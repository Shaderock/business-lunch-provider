package com.shaderock.lunch.blpmini.data.datasource.provider

import com.shaderock.lunch.blpmini.data.datasource.disk.auth.TokenDiskSource
import com.shaderock.lunch.blpmini.data.datasource.memory.auth.TokenMemorySource
import javax.inject.Inject

class TokenProvider @Inject constructor(
    private val diskSource: TokenDiskSource,
    private val memorySource: TokenMemorySource
) {
    val token: String? get() = memorySource.data?.token ?: diskSource.data?.token
}
