/* (C) 2025. All rights reserved. */
package com.racingapp.data.repositories

import com.racingapp.core.PollingManager
import com.racingapp.data.mappers.RaceSummaryMapper
import com.racingapp.data.services.RaceApiService
import com.racingapp.domain.logger.Logger
import com.racingapp.domain.models.RaceDetails
import com.racingapp.domain.repositories.Repository
import com.racingapp.utils.NetworkException
import com.racingapp.utils.NetworkExceptionType
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import okio.IOException
import retrofit2.HttpException

class RemoteRepository
@Inject
constructor(
  private val apiService: RaceApiService,
  private val mapper: RaceSummaryMapper,
  private val pollingManager: PollingManager,
  private val logger: Logger
) : Repository {

  private val _races = MutableSharedFlow<Result<List<RaceDetails>>>()

  override fun startPolling(intervalMillis: Long) {
    pollingManager.startPolling(intervalMillis) { getUpcomingRaces() }
  }

  override fun stopPolling() {
    pollingManager.stopPolling()
  }

  override suspend fun getUpcomingRaces() {
    try {
      val response = apiService.fetchRaces()
      val races = response.data.raceSummaries.values.map { mapper.toDomain(it) }
      _races.emit(Result.success(races))
    } catch (e: SocketTimeoutException) {
      logger.error(e.message ?: "SocketTimeoutException")
      _races.emit(Result.failure(NetworkException(NetworkExceptionType.Timeout)))
    } catch (e: UnknownHostException) {
      logger.error(e.message ?: "UnknownHostException")
      _races.emit(Result.failure(NetworkException(NetworkExceptionType.UnknownHost)))
    } catch (e: SocketException) {
      logger.error(e.message ?: "SocketException")
      _races.emit(Result.failure(NetworkException(NetworkExceptionType.ConnectionError)))
    } catch (e: HttpException) {
      logger.error(e.message ?: "HttpException")
      _races.emit(Result.failure(NetworkException(NetworkExceptionType.ServerError(e.code()))))
    } catch (e: IOException) {
      logger.error(e.message ?: "IOException")
      _races.emit(Result.failure(NetworkException(NetworkExceptionType.NoInternet)))
    } catch (e: CancellationException) {
      logger.error(e.message ?: "Exception")
      _races.emit(Result.failure(NetworkException(NetworkExceptionType.Cancelled)))
    }
  }

  override val races: Flow<Result<List<RaceDetails>>> = _races
}
