package com.example.smartcane.model

class ModelContact {
    var key: String? = null
    var name: String? = null
    var relation: String? = null
    var number: String? = null

    constructor() {}

    constructor(namaContact: String?, relationContact: String?, numberrContact: String?) {
        name = namaContact
        relation = relationContact
        number = numberrContact
    }
}