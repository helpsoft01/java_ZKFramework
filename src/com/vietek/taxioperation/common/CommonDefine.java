package com.vietek.taxioperation.common;

public class CommonDefine {

	public static String COMMON_VALIDATE_FORM_VALUES = "Các trường (*) không được để trống !";
	public static int MAX_LENGTH_CONTENT_FIELD = 255;
	public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	public static final String NUM_PATTERN = "[0-9]+";
	public static final String NUM_CHARACTERS_PATTERN = ".*[^0-9A-Za-z].*";
	public static final int CODE_MAX_LENGTH = 15;
	public static final int NAME_MAX_LENGTH = 25;
	public static final String SEARCH_PATTERN = "\\p{Punct}";
	public static String ERRORS_STRING_IS_EMPTY = " không được để trống !";
	public static String ERRORS_STRING_INVALID = " không hợp lệ !";
	public static String ERRORS_STRING_IS_EXIST = " đã tồn tại !";
	public static String ERRORS_STRING_IS_HAS_SPECIAL = "Tồn tại ký tự đặc biệt";
	public static String ERRORS_STRING_IS_LIMIT_MAXLENGTH = "Vượt quá độ dài cho phép";
	public static String TITLE_NOTIFY_CUSTOMER_DRIVER_REGISTER = "Mai Linh thong bao ";
	public static final String PRE_PHONE_SMS = new String("84");
	public static final String PRE_PHONE_SMS_09 = new String("09");
	public static final String PRE_PHONE_SMS_01 = new String("01");
	public static final String PRE_PHONE_SMS_84 = new String("+84");

	public static final String LOG_USER_ACTION = "LOG_USER_ACTION";
	public static final String LOG_DEBUG = "LOG_DEBUG";
	public static final String LOG_DEBUG_TAXIORDER = "LOG_DEBUG_TAXIORDER";
	public static final String LOG_TRACKING = new String("LOG_TRACKING");
	public static final String LOG_VMAP = "LOG_VMAP";
	public static final String LOG_V_WARNING = "V_Warning";
	/*-- Use for put notification app --*/
	public static final String API_KEY_ANDROID = "AIzaSyCE3jxxjccXtRGSmEx_He8enM-6slX5XCQ";
	public static final String API_KEY_IOS = "AIzaSyCF6_dQY7d4KAcCbRjJqmForbQBNyngmZ4";

	public static final int TYPE_APP_ANDROID = 1;
	public static final int TYPE_APP_IOS = 2;

	public static final int DEFAULT_DISTANCE = 1000;
	public static final int DEFAULT_TIME = 60;

	/** -- Config to send SMS -- **/

	public static final String SMS_URL = "http://221.132.39.104:8083/bsmsws.asmx?wsdl";
	public static final String SMS_USERNAME = "mailinh";
	public static final String SMS_PASSWORD = "P@ss65!@#098";
	public static final String SMS_BRANDNAME = "MAILINHTAXI";
	public static final String SMS_NOTIFY_DRIVER_REGISTER = "Y";

	/*--- --- */

	public static int TRIP_WAIT_TIME_OUT = 60;
	public static int WAIT_TIME_PER_DRIVER = 15;
	public static int MAX_DISTANCE = 1000;
	public static int MAX_DISTANCE_VIEW = 5000;
	public static int MAX_DRIVER_TO_ASK = 5;

	public static int SHOW_OPEN99 = 0;

	public static final String CALL_CENTER_URL = "http://router.hohuy.vn:8008/";
	public static final int TIMEOUT_CALLINWINDOW = 60;
	public static final int TIMEOUT_REFFRESH_DATA_DTV = 15;
	public static final int CALLIN_TIMECHECK = 15;
	public static final int TIMEOUT_CHECKFAILCALLINSTART = 5;
	public static final int MAX_EXTENSION_IN_TABLE = 2;
	public static final String APIKEY_GOOGLE = "AIzaSyATIW_0Lm0Z1_fgGBLZ6tD05wX3oSIR5ok";
	public static final int INDEX_DEFAULT_TYPECAR = 3;
	public static final int DURATION_VALID_TAXIORDER = 45;

	/*-- Config mail Mai Linh --*/
	public static final String EMAIL_SUPPORT = "support@mailinhapp.com";
	public static final String EMAIL_SUPPORT_PASS = "support@123";
	public static final String EMAIL_SERVER_HOST = "smtp.zoho.com";
	public static final String EMAIL_SERVER_PORT = "587";

	/*-- Time search SOS notification : second --*/

	public static final long TIME_VALUE_SOS_NOTIFICATION = 1;
	public static final int MAX_TAXI_ONLINE = 20;

	public static final String TYPE_APP_CUSTOMER = "0";
	public static final String TYPE_APP_DRIVER = "1";

	public static final int TRIP_STATUS_CANCELED = 0;
	public static final int TRIP_STATUS_NEW = 1;
	public static final int TRIP_STATUS_DRIVER_REGISTERED = 2;
	public static final int TRIP_STATUS_DRIVER_COME = 3;
	public static final int TRIP_STATUS_DONE = 4;
	public static final int TRIP_STATUS_RATED = 5;

	public static final String DRIVER_NAME = "Tài xế: ";
	public static final String DRIVER_NUMBER = " Mã: ";
	public static final String TRIP_STATUS_DRIVER_REGISTERED_CONTENT = "đã đăng ký quốc khách của bạn !";
	public static final String TRIP_STATUS_DRIVER_COME_CONTENT = " đang đến địa điểm của bạn !";
	public static final String TRIP_CANCELED_REASON = " đã hủy !";
	public static final int DEFAULT_MINUTE_AFTER_START_TRIP_AIRPORT = 5;

	public static class Tittle {
		public static final String TITLE_BTN_ADD_NEW = "Thêm mới";
		public static final String TITLE_BTN_EDIT = "Sửa";
		public static final String TITLE_BTN_DELETE = "Xóa";
		public static final String TITLE_BTN_REFRESH = "Refresh";
		public static final String TITLE_BTN_SEARCH = "Tìm kiếm";
		public static final String TITLE_BTN_SAVE = "Lưu";
		public static final String TITLE_BTN_UPLOAD = "Upload";
		public static final String TITLE_BTN_CLOSE = "Đóng";
	}

	public static class Customer {
		public static String TITLE_FORM = "Khách hàng";
		public static String FORM_CUSTOMER_EMAIL_INVALID = "Email không hợp lệ !";
		public static String FORM_CUSTOMER_PHONE_NUMBER_INVALID = "Số điện thoại không hợp lệ !";
		public static final String NAME_CUSTOMER = "Tên khách hàng";
		public static final String PHONE_CUSTOMER = "Số điện thoại";
		public static final String EMAIL_CUSTOMER = "Email";
		public static final String NOTE_CUSTOMER = "Ghi chú";
		public static String FORM_CUSTOMER_PASSWORD_LENGHT_INVALID = "Độ dài mật khẩu phải lớn hơn 4 ký tự !";
		public static String FORM_CUSTOMER_PASSWORD_CONFIRM_INVALID = "Nhắc lại mật khẩu không đúng !";
		public static String FORM_CUSTOMER_EXIST_CUSTOMER = "Số điện thoại khách hàng đã tồn tại, Vui lòng kiểm tra lại thông tin !";
	}

	public static class GoogleMap {

		public static String URL_ICON_POINT_START = "./themes/images/beginmapp.png";
		public static String URL_ICON_POINT_END = "./themes/images/endmapp.png";
		public static String URL_ICON_ADDRESS1 = "./themes/images/point_add1_48.png";
		public static String URL_ICON_ADDRESS2 = "./themes/images/point_add2_48.png";
		public static String URL_ICON_ADDRESS3 = "./themes/images/point_add3_48.png";

		public static double MAP_LAT = 21.0031545;
		public static double MAP_LNG = 105.8446598;
		public static int MAP_ZOOM = 15;
		public static int MAP_TYPE = 0;
	}

	public static class TypeVehicleOrder {
		public static String TITLE_FORM = "Loại xe";
		public static final String CODE_TYPEVEHICLEORDER = "Mã xe";
		public static final String NAME_TYPEVEHICLEORDER = "Tên xe";
	}

	public static class SwitchboardTMS {
		public static String TITLE_FORM = "Tổng đài";
		public static final String CODE_SWITCHBOARDTMS = "Mã tổng đài";
		public static final String NAME_SWITCHBOARDTMS = "Tên tổng đài";
	}

	public static class Agent {

		public static final String AGENT_CODE = "Mã chi nhánh";
		public static final String AGENT_NAME = "Tên chi nhánh";
		public static final String AGENT_AGENTTYPE = "Loại hình chi nhánh";
		public static final String AGENT_COMPANY = "Công ty";

	}

	public static class DriverIgnoreTrip {
		public static String TITLE_FROM = "Thông tin hủy cuốc";
		public static final String DRIVER_DRIVER_IGNORE_TRIP = "Lái xe";
		public static final String VEHICLE_DRIVER_IGNORE_TRIP = "Phương tiện";
		public static final String TIME_DRIVER_IGNORE_TRIP = "Thời gian";
	}

	public static class DriverNotification {
		public static String TITLE_FROM = "Thông báo cho lái xe";
	}

	public static class SysCompany {
		public static final String COMPANY = "Công ty";
		public static final String CODE_SYSCOMPANY = "Mã công ty";
		public static final String NAME_SYSCOMPANY = "Tên công ty";
	}

	public static class AnnotationTitle {
		public static String TITLE_NULLABLE = "nullable";
		public static String TITTLE_MIN_LENGHT = "minLength";
		public static String TITTLE_MAX_LENGHT = "maxLength";
		public static String TITTLE_REGEX = "regex";
		public static String TITLE_IS_HAS_SPECIAL_CHAR = "isHasSpecialChar";
		public static String TITLE_IS_EMAIL = "isEmail";
		public static String TITLE_ALOW_EXIST = "alowrepeat";
	}

	public static class Region {
		public static final String TITLE_FORM = "Vùng miền";
		public static final String CODE_REGION = "Mã vùng miền";
		public static final String NAME_REGION = "Tên vùng miền";
	}

	public static class Channel {

		public static final String TITLE_FORM = "Kênh điều đàm";
		public static final String NAME_CHANNEL = "Tên Kênh";
		public static final String CODE_CHANNEL = "Mã kênh";
		public static final String CODE_SWITCHBOARD = "Tổng đài";

	}

	public static class VehicleGroup {
		public static final String TITLE_FORM = "Nhóm xe";
		public static final String CODE_VEHICLE_GROUP = "Mã Nhóm xe";
		public static final String NAME_VEHICLE_GROUP = "Tên Nhóm xe";
		public static final String AGENT_VEHICLE_GROUP = "Chi nhánh";
		public static final String VEHICLE_GROUP_TYPE = "Loại Nhóm xe";
	}

	public static class Abbreviation {

		public static final String TITLE_FORM = "Tên viết tắt";
		public static final String CODE_ABBREVIATION = "Mã viết tắt";
		public static final String NAME_ABBREVIATION = "Tên viết tắt";
	}

	public static class AbbreviationAddress {

		public static final String TITLE_FORM = "Tên địa chỉ viết tắt";
		public static final String CODE_ABB_ADDRESS = "Địa chỉ viết tắt";
		public static final String FULL_ABB_ADDRESS = "Mô tả đầy đủ";
	}

	public static class VehicleTypeDefine {
		public static final String TITLE_FORM = "Loại xe";
		public static final String CODE_TYPE = "Mã loại xe";
		public static final String NAME_TYPE = "Tên loại xe";
		public static final String TRADEMARK = "Hiệu xe";
		public static final String CARSUPPLIER = "Hãng xe";

	}

	public static class SwitchboardDefine {

		public static final String TITLE_FORM = "Tổng đài";
		public static final String CODE_SWITCHBOARD = "Mã tổng đài";
		public static final String NAME_SWITCHBOARD = "Tên tổng đài";
	}

	public static class Zone {

		public static final String TITLE_FORM = "Khu vực";
		public static final String NAME_ZONE = "Tên khu vực";
		public static final String CODE_ZONE = "Mã khu vực";
		public static final String CODE_REGION = "Vùng miền";

	}

	public static class ZoneDefine {

		public static final String TITLE_FORM = "Khu vực";
		public static final String CODE_ZONE = "Mã khu vực";
		public static final String NAME_ZONE = "Tên khu vực";
	}

	public static class DistrictTms {

		public static final String TITLE_FORM = "Quận/ Huyện";
		public static final String NAME_DISTRICTTMS = "Tên quận/ huyện";
		public static final String CODE_DISTRICTTMS = "Mã quận/ huyện";
		public static final String CODE_PROVINCE = "Tỉnh thành";

	}

	public static class DistrictTmsDefine {

		public static final String TITLE_FORM = "Quận/ Huyện";
		public static final String CODE_DISTRICTTMS = "Mã quận/ huyện";
		public static final String NAME_DISTRICTTMS = "Tên quận/ huyện";
	}

	public static class Province {

		public static final String TITLE_FORM = "Tỉnh thành";
		public static final String NAME_PROVINCE = "Tên tỉnh thành";
		public static final String CODE_PROVINCE = "Mã tỉnh thành";
		public static final String CODE_NATIONAL = "Quốc gia";

	}

	public static class ProvinceDefine {

		public static final String TITLE_FORM = "Tỉnh thành";
		public static final String CODE_PROVINCE = "Mã tỉnh thành";
		public static final String NAME_PROVINCE = "Tên tỉnh thành";
	}

	public static class ParkingAreaDefine {
		public static final String TITLE_FORM = "Bãi giao ca";
		public static final String CODE_PARKINGAREA = "Mã bãi giao ca";
		public static final String NAME_PARKINGAREA = "Tên bãi giao ca";
		public static final String GROUP_PARKING = "Đội xe";
		public static final String ADDRESS = "Địa chỉ";
	}

	public static class ShiftworkTmsDefine {
		public static final String TITLE_FORM = "Ca làm việc";
		public static final String CODE_SHIFTWORK = "Mã ca";
		public static final String TIME_INPUT = "Thời gian";
	}

	public static class TypeTablePriceDefine {
		public static final String TITLE_FORM = "Mã loại bảng giá";
		public static final String CODE_TYPE_TABLE_PRICE = "Mã loại bảng giá";
		public static final String NAME_TYPE_TABLE_PRICE = "Tên loại bảng giá";
	}

	public static class TablePriceDefine {
		public static final String TABLE_PRICE_KM = "Đoạn đường đi";
		public static final String TABLE_PRICE_TIME = "Thời gian đi";
		public static final String TABLE_PRICE_MONEY = "Giá đi";
		public static final String TABLE_PRICE_DATE = "Thời gian áp dụng";
	}

	public static class CallCenterDefine {
		public static final String TITLE_FORM = "CallCenter";
		public static final String NAME_CALLCENTER = "Tên CallCenter";
		public static final String PHONE_CALLCENTER = "Số điện thoại";

	}

	public static class FeedBackCustomer {
		public static final String TITLE_FORM = "Đánh giá từ người dùng";
		public static final String TYPE_FEED = "Loại đánh giá";
		public static final String NAME_USE = "Người dùng hoặc lái xe";
		public static final String VALUE_POIN = "Điểm đánh giá";
	}

	public static class TelephoneTableTms {
		public static final String CODE_TELTABLE = "Mã bàn làm việc";
		public static final String NAME_TELTABLE = "Tên bàn làm việc";
	}

	public static class TaxiMarker {
		public static final String ICON_4SEATS_NON_PROCESSING = "./themes/images/marker_4s_kokhach.png";
		public static final String ICON_4SEATS_PROCESSING = "./themes/images/marker_4s_cokhach.png";
		public static final String ICON_4SEARS_CANCEL_TRIP = "./themes/images/marker_4s_cancel.png";
		public static final String ICON_4SEATS_NOT_PASS_TRIP = "./themes/images/marker_4s_kokhach.png";
		public static final String ICON_7SEATS_NON_PROCESSING = "./themes/images/marker_7s_kokhach.png";
		public static final String ICON_7SEATS_PROCESSING = "./themes/images/marker7s_32.png";
	}

	public static class TaxiIcon {
		public static final String ICON_4SEATS_NON_PROCESSING = "./themes/images/Vehicles/icon_4seats_kokhach.png";
		public static final String ICON_4SEATS_PROCESSING = "./themes/images/Vehicles/icon_4seats_cokhach.png";
		public static final String ICON_4SEATS_PAUSE = "./themes/images/Vehicles/icon_4seats_pause.png";
		public static final String ICON_4SEATS_STOP = "./themes/images/Vehicles/icon_4seats_stop.png";
		public static final String ICON_7SEATS_NON_PROCESSING = "./themes/images/Vehicles/icon_7seats_kokhach.png";
		public static final String ICON_7SEATS_PROCESSING = "./themes/images/Vehicles/icon_7seats_cokhach.png";
		public static final String ICON_7SEATS_PAUSE = "./themes/images/Vehicles/icon_7seats_pause.png";
		public static final String ICON_7SEATS_STOP = "./themes/images/Vehicles/icon_7seats_stop.png";
		public static final String ICON_LOST_GPS = "./themes/images/Vehicles/icon_lost_gps.png";
		public static final String ICON_LOST_GSM = "./themes/images/Vehicles/icon_lost_gsm.png";
		public static final String ICON_MAINTAIN = "./themes/images/Vehicles/icon_maintain.png";
		public static final String ICON_MAINTAIN_16PX = "./themes/images/Vehicles/icon_maintain_16.png";
	}

	public static class MarketingPlace {

		public static final String TITLE_FORM = "Điểm tiếp thị";
		public static final String CODE = "Mã điểm tiếp thị";
		public static final String NAME = "Tên điểm tiếp thị";
		public static final String AGENT = "Chi nhánh";
		public static final String CARD = "Thẻ nhân viên";
		public static final String ADDRESS = "Địa chỉ";
		public static final String TYPEPOINT = "Loại điểm";
	}

	public static class CustomerNotification {
		public static final String NOTICE_NAME = "Tên thông báo";
		public static final String NOTICE_OBJECT = "Tiêu đề thông báo";
		public static final String NOTICE_F_DATE = "Thời gian bắt đầu";
		public static final String NOTICE_T_DATE = "Thời gian kết thúc";
		public static final String NOTICE_TYPE = "Loại hình";
	}

	public static class DeviceTxm {
		public static final String IMEI_DEVICE = "Số IMEI";
		public static final String SIM_NUMBER = "Số Sim";
		public static final String AGENT_DEVICE = "Chi nhánh";
	}

	public static class DriverTxm {
		public static final String DRIVER_AGENT = "Chi nhánh";
		public static final String DRIVER_NAME = "Tên Lái Xe";
		public static final String DRIVER_CODE = "Mã số nhân viên";
		public static final String DRIVER_PHONE = "Văn hóa số";
	}

	public static class VehicleTxm {
		public static final String VEHICLE_NUMBER = "Số tài";
		public static final String LICENSE_PLACE = "Biển kiểm soát";
		public static final String DEVICE = "Thiết bị";
		public static final String VEHICLE_GROUP = "Nhóm xe";
		public static final String VEHICLE_TYPE = "Loại xe";
		public static final String TYPE_MAR_TAXI = "Loại hình";

	}
}
