package com.slaycard.basic.cqrs

import com.slaycard.basic.ResultT

interface QueryHandler<TQuery, TQueryOut> {
    fun handle(query: TQuery): ResultT<TQueryOut>
}
