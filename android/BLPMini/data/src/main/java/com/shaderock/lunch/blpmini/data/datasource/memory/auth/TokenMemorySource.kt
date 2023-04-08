package com.shaderock.lunch.blpmini.data.datasource.memory.auth

import javax.inject.Inject

class TokenMemorySource @Inject constructor() {
    var data: TokenMemoryModel? = null
}
