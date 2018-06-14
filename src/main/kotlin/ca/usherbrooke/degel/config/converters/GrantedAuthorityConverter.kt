package ca.usherbrooke.degel.config.converters

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class GrantedAuthorityConverter : AttributeConverter<GrantedAuthority, String> {
    override fun convertToDatabaseColumn(attribute: GrantedAuthority) = attribute.authority!!

    override fun convertToEntityAttribute(dbData: String) = SimpleGrantedAuthority(dbData)
}