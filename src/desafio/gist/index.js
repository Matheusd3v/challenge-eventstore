const schema = require("./schema");
const facts = require("./facts");
const console = require('console');

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

const validateAttribute = (entity, fact) => {
    const factCardinality = schemaCardinality()[fact[1]].cardinality

    if (factCardinality === "one"){
        if(entity.attributes[fact[1]].length > 0) {
            entity.attributes[fact[1]] = [fact[1], fact[2], fact[3]]

            return entity
        }

        entity.attributes[fact[1]].push(fact[1], fact[2], fact[3])

        return entity
    }

    if (entity.attributes[fact[1]].length < 1) {
        entity.attributes[fact[1]].push([fact[1], fact[2], fact[3]])
        return entity
    }

    const indexSimilarFact = entity.attributes[fact[1]].findIndex(attribute =>attribute[1] === fact[2])
    
    if (indexSimilarFact === -1) {
        entity.attributes[fact[1]].push([fact[1], fact[2], fact[3]])
        
        return entity
    }

    const trueValue = entity.attributes[fact[1]][indexSimilarFact][1]

    if (trueValue && !fact[3]) {
        entity.attributes[fact[1]].splice(indexSimilarFact, 1)
    }

   return entity
}


const controllerEvents = (eventStore) => {
    const entitys = []
    let entitysData = []
    
    for (const event of eventStore) {
        if(!entitys.includes(event[0])) {
            entitys.push(event[0])
            const newEntity = buildEntity(event)
            entitysData.push(newEntity)
        }    
        
        const entityIndex = entitysData.findIndex(object => object.entity === event[0])      
        const entity = entitysData[entityIndex]
        
        const newEntity = validateAttribute(entity, event)

        entitysData.splice(entityIndex, 1, newEntity)
    }

    return entitysData
}

const updateState = (facts) => {
    const entitys  = controllerEvents(facts)

    const currentState = [];
    
    for (const entity of entitys) {
   
        const values = [...Object.values(entity.attributes)]

        values.forEach(event => {

            if (typeof event[0] === "string") {
                
                currentState.push([entity.entity, ...event])
            } else {
            
                event.forEach(current => {
                    currentState.push([entity.entity,...current])
                })
            }
            
        })
    }
    
    return currentState
}


updateState(facts)


