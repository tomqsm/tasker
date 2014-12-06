package json

import groovy.json.JsonBuilder

class Jsoner {
    def objectToJson(Object o){
        new JsonBuilder(o).toPrettyString();
    }
}

