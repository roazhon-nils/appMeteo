package com.example.sae4

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

//@Serializable(with = WeatherCode.WeatherCodeSerializer::class)
enum class WeatherCode( val enum : Int ) {
    ClearSky(0),
    MainlyClear(1),
    PartlyCloudy(2),
    Overcast(3),
    Fog(45),
    DepositingRimeFog(48),
    DrizzleLight(51),
    DrizzleModerate(53),
    DrizzleDense(55),
    RainSlight(61),
    RainModerate(63),
    RainHeavy(65),
    FreezingRainLight(66),
    FreezingRainHeavy(67),
    SnowFallSlight(71),
    SnowFallModerate(73),
    SnowFallHeavy(75),
    SnowGrains(77),
    RainShowerSlight(80),
    RainShowerModerate(81),
    RainShowerViolent(82),
    SnowShowerSlight(85),
    SnowShowerHeavy(86),
    ThunderstormSlightRain(96),
    ThunderststormHeavyRain(99)

    /*@Serializer(forClass = WeatherCode::class)
    object WeatherCodeSerializer: KSerializer<WeatherCode>{

        fun serialize(value: Int): EnumInstance{
            return EnumInstance(value)
        }
    }*/

}

