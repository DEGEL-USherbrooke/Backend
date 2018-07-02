package ca.usherbrooke.degel.config.converters

import biweekly.Biweekly
import biweekly.ICalendar
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class ICalendarConverter : AttributeConverter<ICalendar, String> {
    override fun convertToEntityAttribute(dbData: String?): ICalendar? {
        if (dbData == null)
            return null

        return Biweekly.parseJson(dbData).first()
    }

    override fun convertToDatabaseColumn(attribute: ICalendar?): String? {
        if (attribute == null)
            return null

        return Biweekly.writeJson(attribute).go()
    }

}