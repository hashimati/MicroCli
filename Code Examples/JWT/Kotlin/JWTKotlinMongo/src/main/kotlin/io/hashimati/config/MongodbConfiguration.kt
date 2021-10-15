package io.hashimati.config


import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.core.naming.Named
import java.util.HashMap


@ConfigurationProperties("mdb")
interface MongodbConfiguration : Named {
    fun getCollections() :HashMap<String, String>;
}