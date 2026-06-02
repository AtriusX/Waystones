package xyz.atrius.waystones.config

import org.bstats.bukkit.Metrics
import org.bstats.charts.AdvancedPie
import org.bstats.charts.DrilldownPie
import org.bstats.charts.SimpleBarChart
import org.bstats.charts.SimplePie
import org.bstats.charts.SingleLineChart
import org.bukkit.Material
import org.koin.core.annotation.Module
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.property.type.Power
import xyz.atrius.waystones.internal.KotlinPlugin
import xyz.atrius.waystones.manager.ConfigManager
import xyz.atrius.waystones.repository.WaystoneInfoRepository
import xyz.atrius.waystones.utility.bindAsEnum
import java.util.Locale
import java.util.concurrent.TimeUnit

@Module
object MetricsModule {

    const val BSTATS_PLUGIN_ID: Int = 29245

    private val playerCountSplits = splitsOf(5, 20, 50, 100, 200)
    private val maxPlayersSplits = splitsOf(10, 25, 50, 100, 200, 500)
    private val worldCountSplits = splitsOf(3, 6, 10, 20)
    private val baseDistanceSplits = splitsOf(49, 99, 199, 499, 999)
    private val maxBoostSplits = splitsOf(24, 49, 99, 149, 249)
    private val maxWarpSizeSplits = splitsOf(24, 49, 99, 199)
    private val waystoneCountSplits = splitsOf(25, 50, 100, 200, 500)

    @Single
    fun metrics(
        @Provided plugin: KotlinPlugin,
        @Provided configManager: ConfigManager,
        @Provided waystoneInfoRepository: WaystoneInfoRepository,
    ): Metrics = Metrics(plugin, BSTATS_PLUGIN_ID).apply {
        addCustomChart(SimplePie("database_type") {
            plugin.config
                .getString("type")
                .bindAsEnum<SupportedDatabase>()
                .description
        })

        addCustomChart(SimplePie("power_mode") {
            configManager
                .getPropertyOrNull<Power>("require-power")
                ?.value()
                ?.toString()
                ?: "INTER_DIMENSION"
        })

        addCustomChart(SimplePie("fallback_locale") {
            configManager
                .getPropertyOrNull<Locale>("fallback-locale")
                ?.value()
                ?.getDisplayLanguage(Locale.ENGLISH)
                ?: "English"
        })

        addCustomChart(SimpleBarChart("feature_adoption") {
            mapOf(
                "Portal Sickness" to configManager.booleanFlag("enable-portal-sickness"),
                "Single-Use Keys" to configManager.booleanFlag("single-use"),
                "Uses Custom Recipe" to configManager.customRecipeFlag("key-recipe"),
            )
        })

        addCustomChart(DrilldownPie("player_count") {
            drilldown(plugin.server.onlinePlayers.size, playerCountSplits)
        })

        addCustomChart(DrilldownPie("max_players") {
            drilldown(plugin.server.maxPlayers, maxPlayersSplits)
        })

        addCustomChart(DrilldownPie("world_count") {
            drilldown(plugin.server.worlds.size, worldCountSplits)
        })

        addCustomChart(DrilldownPie("base_distance") {
            drilldown(configManager.intProperty("base-distance"), baseDistanceSplits)
        })

        addCustomChart(DrilldownPie("max_boost") {
            drilldown(configManager.intProperty("max-boost"), maxBoostSplits)
        })

        addCustomChart(DrilldownPie("max_warp_size") {
            drilldown(configManager.intProperty("max-warp-size"), maxWarpSizeSplits)
        })

        addCustomChart(AdvancedPie("world_environments") {
            plugin.server.worlds
                .groupBy { it.environment.name }
                .mapValues { it.value.size }
        })

        addCustomChart(SingleLineChart("waystone_count") {
            waystoneInfoRepository.entries().get(1, TimeUnit.SECONDS)
        })

        addCustomChart(DrilldownPie("waystone_count_bucketed") {
            drilldown(waystoneInfoRepository.entries().get(1, TimeUnit.SECONDS), waystoneCountSplits)
        })
    }

    private class Splits(private val points: IntArray) {

        fun resolve(value: Int): String {
            for (i in points.indices) {
                // Skip all buckets where the current end point is less than the input value
                if (value > points[i]) {
                    continue
                }
                // Cap lower end at 0, otherwise get the value of the previous point in line and add 1
                val start = when {
                    i <= 0 -> 0
                    else   -> points[i - 1] + 1
                }
                // Get the value of the current point and construct the range
                return "$start-${points[i]}"
            }
            // Default case, anything over the max of all buckets
            return "${points.last() + 1}+"
        }
    }

    private fun splitsOf(vararg points: Int) = Splits(intArrayOf(*points))

    private fun drilldown(value: Int, splits: Splits): Map<String, Map<String, Int>> =
        mapOf(splits.resolve(value) to mapOf(value.toString() to 1))

    private fun ConfigManager.booleanFlag(key: String): Int = when (
        getPropertyOrNull<Boolean>(key)?.value()
    ) {
        true -> 1
        else -> 0
    }

    private fun ConfigManager.intProperty(key: String): Int =
        getPropertyOrNull<Int>(key)
            ?.value()
            ?: 0

    private fun ConfigManager.customRecipeFlag(key: String): Int {
        val prop = getListPropertyOrNull<List<Material>>(key)
            ?: return 0
        // If the lists have equality, then its assumed the recipe has not changed
        if (prop.value() == prop.default) {
            return 0
        }

        return 1
    }
}
