const schema = require("./schema");
const facts = require("./facts");
const console = require('console');

const currentState = [];

const eventController = (facts) => {

};



const schemaCardinality = () => {
    const newSchema = {}

    schema.forEach(currentSchema => {
        newSchema[currentSchema[0]] = { cardinality: currentSchema[2]}
    })

    return newSchema
}

const setAttributeSctructure = (schema) => {
    const fields = {}

    schema.forEach(currentSchema => {
        fields[currentSchema[0]] = []
    });
    
    return fields
}

const buildEntity = (fact) => {
    const entity = {}

    entity.entity = fact[0]
    entity.attributes = setAttributeSctructure(schema)
    
    if (schemaCardinality()[fact[1]].cardinality === "one") {
        entity.attributes[fact[1]] = [fact[1] ,fact[2], fact[3]]

        return entity
    }

    entity.attributes[fact[1]].push([fact[1], fact[2], fact[3]])

    return entity
}

