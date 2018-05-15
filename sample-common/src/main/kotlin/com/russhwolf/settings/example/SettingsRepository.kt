package com.russhwolf.settings.example

import kotlin.properties.ReadWriteProperty

import com.russhwolf.settings.Settings
import com.russhwolf.settings.boolean
import com.russhwolf.settings.double
import com.russhwolf.settings.float
import com.russhwolf.settings.int
import com.russhwolf.settings.long
import com.russhwolf.settings.nullableBoolean
import com.russhwolf.settings.nullableDouble
import com.russhwolf.settings.nullableFloat
import com.russhwolf.settings.nullableInt
import com.russhwolf.settings.nullableLong
import com.russhwolf.settings.nullableString
import com.russhwolf.settings.string

class SettingsRepository(private var settings: Settings) {
    val mySettings: List<SettingConfig<*>> = listOf(
        StringSettingConfig(settings, "MY_STRING"),
        IntSettingConfig(settings, "MY_INT"),
        LongSettingConfig(settings, "MY_LONG"),
        FloatSettingConfig(settings, "MY_FLOAT"),
        DoubleSettingConfig(settings, "MY_DOUBLE"),
        BooleanSettingConfig(settings, "MY_BOOLEAN"),
        NullableStringSettingConfig(settings, "MY_NULLABLE_STRING"),
        NullableIntSettingConfig(settings, "MY_NULLABLE_INT"),
        NullableLongSettingConfig(settings, "MY_NULLABLE_LONG"),
        NullableFloatSettingConfig(settings, "MY_NULLABLE_FLOAT"),
        NullableDoubleSettingConfig(settings, "MY_NULLABLE_DOUBLE"),
        NullableBooleanSettingConfig(settings, "MY_NULLABLE_BOOLEAN")
    )

    fun clear() = settings.clear()
}

open class SettingConfig<T>(
    private val settings: Settings,
    val key: String,
    defaultValue: T,
    delegate: Settings.(String, T) -> ReadWriteProperty<Any?, T>,
    private val toType: String.() -> T
) {
    private var value: T by settings.delegate(key, defaultValue)

    fun remove(): Unit = settings.remove(key)
    fun exists(): Boolean = settings.contains(key)
    fun get(): String = value.toString()
    fun set(value: String): Boolean {
        return try {
            this.value = value.toType()
            true
        } catch (exception: Exception) {
            false
        }
    }

    override fun toString() = key
}

open class NullableSettingConfig<T>(
    settings: Settings,
    key: String,
    delegate: Settings.(String) -> ReadWriteProperty<Any?, T?>,
    toType: String.() -> T
) : SettingConfig<T?>(settings, key, null, { it, _ -> delegate(it) }, toType)

class StringSettingConfig(settings: Settings, key: String, defaultValue: String = "") :
    SettingConfig<String>(settings, key, defaultValue, Settings::string, { this })

class IntSettingConfig(settings: Settings, key: String, defaultValue: Int = 0) :
    SettingConfig<Int>(settings, key, defaultValue, Settings::int, String::toInt)

class LongSettingConfig(settings: Settings, key: String, defaultValue: Long = 0) :
    SettingConfig<Long>(settings, key, defaultValue, Settings::long, String::toLong)

class FloatSettingConfig(settings: Settings, key: String, defaultValue: Float = 0f) :
    SettingConfig<Float>(settings, key, defaultValue, Settings::float, String::toFloat)

class DoubleSettingConfig(settings: Settings, key: String, defaultValue: Double = 0.0) :
    SettingConfig<Double>(settings, key, defaultValue, Settings::double, String::toDouble)

class BooleanSettingConfig(settings: Settings, key: String, defaultValue: Boolean = false) :
    SettingConfig<Boolean>(settings, key, defaultValue, Settings::boolean, String::toBoolean)

class NullableStringSettingConfig(settings: Settings, key: String) :
    NullableSettingConfig<String>(settings, key, Settings::nullableString, { this })

class NullableIntSettingConfig(settings: Settings, key: String) :
    NullableSettingConfig<Int>(settings, key, Settings::nullableInt, String::toInt)

class NullableLongSettingConfig(settings: Settings, key: String) :
    NullableSettingConfig<Long>(settings, key, Settings::nullableLong, String::toLong)

class NullableFloatSettingConfig(settings: Settings, key: String) :
    NullableSettingConfig<Float>(settings, key, Settings::nullableFloat, String::toFloat)

class NullableDoubleSettingConfig(settings: Settings, key: String) :
    NullableSettingConfig<Double>(settings, key, Settings::nullableDouble, String::toDouble)

class NullableBooleanSettingConfig(settings: Settings, key: String) :
    NullableSettingConfig<Boolean>(settings, key, Settings::nullableBoolean, String::toBoolean)