package com.abdelmageed.baimstask.data.remote.dto.util

interface EntityMapper<Entity, DomainModel> {
    fun mapFromEntity(entity: Entity): DomainModel
}