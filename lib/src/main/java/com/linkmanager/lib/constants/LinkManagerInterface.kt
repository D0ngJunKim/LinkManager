package com.linkmanager.lib.constants

/**
 * Created by 180842 on 2023/11/07.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class LinkManagerInterface(val description: String)

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class LinkManagerException(val description: String)