package com.sesac.gmd.common

import com.sesac.gmd.data.model.Location

// FIXME:
fun Location.isinitialized(): Boolean {
    return this.state != ""
}