package xyz.atrius.waystones.repository

import org.flywaydb.core.internal.jdbc.RowMapper
import java.sql.ResultSet

object CountRowMapper : RowMapper<Int> {

    override fun mapRow(rs: ResultSet): Int =
        rs.getInt(1)
}
