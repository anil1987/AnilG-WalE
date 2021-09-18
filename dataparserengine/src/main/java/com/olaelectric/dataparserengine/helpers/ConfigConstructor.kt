package com.olaelectric.dataparserengine.helpers

import com.olaelectric.dataparserengine.data.CanSignalSnapshot.messageMap
import com.olaelectric.dataparserengine.domain.DBCEntity

class ConfigConstructor {
    fun constructMessageMap(dbcEntity: DBCEntity) {
        for (message in dbcEntity.messages)

            messageMap[message.id] = message.signals
    }
}
