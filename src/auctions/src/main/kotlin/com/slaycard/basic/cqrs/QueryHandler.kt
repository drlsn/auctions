package com.slaycard.basic.cqrs

import com.slaycard.basic.ResultT
import kotlinx.coroutines.Deferred

interface QueryHandler<TQuery, TQueryOut> {
    suspend fun handle(query: TQuery): Deferred<ResultT<TQueryOut>>
}
