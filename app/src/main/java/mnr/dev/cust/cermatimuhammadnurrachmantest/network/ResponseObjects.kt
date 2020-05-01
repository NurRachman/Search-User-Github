package mnr.dev.cust.cermatimuhammadnurrachmantest.network

class ResponseObjects<T>(var items: T) {
    var version = ""
    var datetime = ""
    var timestamp = 0
    var status = ""
    var code = 0
    var message = ""
    var errors = ""
    var documentation_url = ""
}