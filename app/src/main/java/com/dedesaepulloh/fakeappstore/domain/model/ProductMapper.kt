package com.dedesaepulloh.fakeappstore.domain.model

import com.dedesaepulloh.fakeappstore.data.local.CartEntity
import com.dedesaepulloh.fakeappstore.data.model.ProductResponseItem

fun ProductResponseItem.toDomain(): Product {
    return Product(
        id = this.id,
        title = this.title,
        price = this.price,
        description = this.description,
        image = this.image,
        category = this.category
    )
}

fun CartEntity.toDomain(): Product {
    return Product(
        id = this.id,
        title = this.title,
        price = this.price,
        description = this.description,
        image = this.image,
        category = this.category,
        quantity = this.quantity
    )
}

fun Product.toEntity(): CartEntity {
    return CartEntity(
        id = this.id,
        title = this.title,
        price = this.price,
        description = this.description,
        image = this.image,
        category = this.category,
        quantity = this.quantity
    )
}
