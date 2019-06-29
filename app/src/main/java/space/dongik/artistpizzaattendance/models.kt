package space.dongik.artistpizzaattendance


data class AttendRequest(val member_id: String)

data class RequestResult(val message: String)

data class AskResult(val is_attendable:Boolean, val message: String, val member_id: String)

data class AttendResult(val result:Boolean, val message: String)

data class ExtendRequest(val member_id: String, val course: String, val weeks: Int)

data class RegisterRequest(val name:String, val pin:String)
//data class Member(val _id: String,
//                  val name: String,
//                  val phone: String,
//                  val count: Int,
//                  val credit: Int,
//                  val expire_date: String,
//                  val course: String)

data class Attendee(val memberName : String, val phoneNumber: String)

//open class Member(
//    @PrimaryKey
//    @SerializedName(value = "phone_number")
//    var phoneNumber:String,
//    var name:String,
//    var count:Int,
//    var course:String,
//    @SerializedName(value = "expire_date")
//    var expireDate:Int
//): RealmObject() {
//}


//open class Rollbook(
//    var course : String,
//    var date : Int, // phone number and name
//    val attendees: ArrayList<Attendee>
//): RealmObject() {
//}


//data class Attend(val course_name: String, val check: Boolean)

//data class Rollbook(val _id: String, val check: Map<String, Attend>)

//data class Rollbook(val name: String, val weekday: Int, )