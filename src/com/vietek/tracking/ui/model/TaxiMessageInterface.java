package com.vietek.tracking.ui.model;

import java.text.SimpleDateFormat;

public interface TaxiMessageInterface {

	/**
	 * Tien to tra loi ban tin taxi
	 */
	public static final String PREFIX_RESPOND = new String("RES:TXI=");

	/**
	 * Hau to tra loi ban tin taxi
	 */
	public static final String SUFFIX_RESPOND = new String("\r\n");

	/**
	 * value = 0
	 */
	public static final Integer MSG_TYPE_0_CUOC_KHACH = 0;

	/**
	 * value = 1
	 */
	public static final Integer MSG_TYPE_1_CHOT_CA = 1;

	/**
	 * value = 2
	 */
	public static final Integer MSG_TYPE_2_CHOT_CA_LOST = 2;

	/**
	 * value = 3
	 */
	public static final Integer MSG_TYPE_3_HOI_DIA_CHI = 3;

	/**
	 * value = 4
	 */
	public static final Integer MSG_TYPE_4_HOI_CAU_HINH_DONG_HO = 4;

	/**
	 * value = 5
	 */
	public static final Integer MSG_TYPE_5_MA_LOI = 5;

	/**
	 * value = 6
	 */
	public static final Integer MSG_TYPE_6_HOI_TRANG_THAI_THE = 6;

	/**
	 * value = 7
	 */
	public static final Integer MSG_TYPE_7_LEN_CA = 7;

	/**
	 * value = 8
	 */
	public static final Integer MSG_TYPE_8_BAO_LOI = 8;

	/**
	 * 0000.0000.0000.0001 = 0x0001 = 2^00 = 001
	 */
	public static final Integer TAXI_STATUS_IR1 = 0x0001;

	/**
	 * 0000.0000.0000.0010 = 0x0002 = 2^01 = 002
	 */
	public static final Integer TAXI_STATUS_IR2 = 0x0002;

	/**
	 * 0000.0000.0000.0100 = 0x0004 = 2^02 = 004
	 */
	public static final Integer TAXI_STATUS_IR3 = 0x0004;

	/**
	 * 0000.0000.0000.1000 = 0x0008 = 2^03 = 008
	 */
	public static final Integer TAXI_STATUS_PRINTED = 0x0008;

	/**
	 * 0000.0000.0001.0000 = 0x0010 = 2^04 = 016
	 */
	public static final Integer TAXI_STATUS_GSM = 0X0010;

	/**
	 * 0000.0000.0010.0000 = 0x0020 = 2^05 = 032
	 */
	public static final Integer TAXI_STATUS_GPS = 0X0020;

	/**
	 * 0000.0000.0100.0000 = 0x0040 = 2^06 = 064
	 */
	public static final Integer TAXI_STATUS_SEND_2_BLACKBOX = 0X0040;

	/**
	 * 0000.0000.1000.0000 = 0x0080 = 2^07 = 128
	 */
	public static final Integer TAXI_STATUS_LAMP = 0X0080;

	/**
	 * 0000.0001.0000.0000 = 0x0100 = 2^08 = 256
	 */
	public static final Integer TAXI_STATUS_PRINT_STATUS_1 = 0X0100;

	/**
	 * 0000.0010.0000.0000 = 0x0200 = 2^09 = 512
	 */
	public static final Integer TAXI_STATUS_PRINT_STATUS_2 = 0X0200;

	/**
	 * 0000.0100.0000.0000 = 0x0400 = 2^10 = 1024
	 */
	public static final Integer TAXI_STATUS_HEADLAMP = 0X0400;

	/**
	 * 0000.1000.0000.0000 = 0x0800 = 2^11 = 2048
	 */
	public static final Integer TAXI_STATUS_PAYMENT_FORM = 0X800;

	/**
	 * 0001.0000.0000.0000 = 0x1000 = 2^12 = 4096
	 */
	public static final Integer TAXI_STATUS_PAYMENT_TYPE = 0X1000;

	/**
	 * 0010.0000.0000.0000 = 0x2000 = 2^13 = 8192
	 */
	public static final Integer TAXI_STATUS_QUANTITY_CUSTOMER_1 = 0X2000;

	/**
	 * 0100.0000.0000.0000 = 0x4000 = 2^14 = 16384
	 */
	public static final Integer TAXI_STATUS_QUANTITY_CUSTOMER_2 = 0X4000;

	/**
	 * 1000.0000.0000.0000 = 0x8000 = 2^15 = 32768
	 */
	public static final Integer TAXI_STATUS_LOI_XUNG = 0X8000;

	/**
	 * Format date hh:mm:ss MM/dd/yy
	 */
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"hh:mm:ss MM/dd/yy");

	/**
	 * value = 0
	 */
	public static final Integer TAXI_THE_HOP_LE = 0;

	/**
	 * value = 0
	 */
	public static final Integer TAXI_THE_KHONG_HOP_LE = 1;

	/**
	 * value = 2
	 */
	public static final Integer TAXI_THE_HET_HAN = 2;

	/**
	 * value = 3
	 */
	public static final Integer TAXI_THE_KHONG_DU_HAN_MUC = 3;

	/**
	 * value = 4
	 */
	public static final Integer TAXI_THE_BI_PHONG_TOA = 4;

	/**
	 * 0000.0000.0000.0001 = 0x0001 = 2^00 = 001
	 */
	public static final Integer TRACKING_STATUS_ENGINE = 0x0001;

	/**
	 * 0000.0000.0000.0010 = 0x0002 = 2^01 = 002
	 */
	public static final Integer TRACKING_STATUS_DOOR = 0x0002;

	/**
	 * 0000.0000.0000.0100 = 0x0004 = 2^02 = 004
	 */
	public static final Integer TRACKING_STATUS_AIRCONDITIONER = 0x0004;

	/**
	 * 0000.0000.0000.1000 = 0x0008 = 2^03 = 008
	 */
	public static final Integer TRACKING_STATUS_SOS = 0x0008;

	/**
	 * 0000.0000.0001.0000 = 0x0010 = 2^04 = 016
	 */
	public static final Integer TRACKING_STATUS_DIGITAL1 = 0X0010;

	/**
	 * 0000.0000.0010.0000 = 0x0020 = 2^05 = 032
	 */
	public static final Integer TRACKING_STATUS_DIGITAL2 = 0X0020;

	/**
	 * 0000.0000.0100.0000 = 0x0040 = 2^06 = 064
	 */
	public static final Integer TRACKING_STATUS_GSM = 0X0040;

	/**
	 * 0000.0000.1000.0000 = 0x0080 = 2^07 = 128
	 */
	public static final Integer TRACKING_STATUS_GPS = 0X0080;

	/**
	 * 0000.0001.0000.0000 = 0x0100 = 2^08 = 256
	 */
	public static final Integer TRACKING_STATUS_SDCARD1 = 0X0100;

	/**
	 * 0000.0010.0000.0000 = 0x0200 = 2^09 = 512
	 */
	public static final Integer TRACKING_STATUS_SDCARD2 = 0X0200;

	/**
	 * 0000.0100.0000.0000 = 0x0400 = 2^10 = 1024
	 */
	public static final Integer TRACKING_STATUS_FM25V02 = 0X0400;

	/**
	 * 0000.1000.0000.0000 = 0x0800 = 2^11 = 2048
	 */
	public static final Integer TRACKING_STATUS_S25FL256 = 0X800;

	/**
	 * 0001.0000.0000.0000 = 0x1000 = 2^12 = 4096
	 */
	public static final Integer TRACKING_STATUS_GPS_ANTENASHORT = 0X1000;

	/**
	 * 0010.0000.0000.0000 = 0x2000 = 2^13 = 8192
	 */
	public static final Integer TRACKING_STATUS_GPS_ANTENAOPEN = 0X2000;

	/**
	 * 0100.0000.0000.0000 = 0x4000 = 2^14 = 16384
	 */
	public static final Integer TRACKING_STATUS_OPTION_1 = 0X4000;

	/**
	 * 1000.0000.0000.0000 = 0x8000 = 2^15 = 32768
	 */
	public static final Integer TRACKING_STATUS_OPTION_2 = 0X8000;

	/**
	 * 0000.0000.0000.0001 = 0x0001 = 2^00 = 001
	 */
	public static final Integer TRACKING_TAXI_STATUS_LINKED_DEVICE = 0x0001;

	/**
	 * 0000.0000.0000.0010 = 0x0002 = 2^01 = 002
	 */
	public static final Integer TRACKING_TAXI_STATUS_IN_TRIP = 0x0002;

	/**
	 * 0000.0000.0000.0100 = 0x0004 = 2^02 = 004
	 */
	public static final Integer TRACKING_TAXI_STATUS_IR_STATE_1 = 0x0004;

	/**
	 * 0000.0000.0000.1000 = 0x0008 = 2^03 = 008
	 */
	public static final Integer TRACKING_TAXI_STATUS_IR_STATE_2 = 0x0008;

	/**
	 * 0000.0000.0001.0000 = 0x0010 = 2^04 = 016
	 */
	public static final Integer TRACKING_TAXI_STATUS_PRINT_STATE_1 = 0X0010;

	/**
	 * 0000.0000.0010.0000 = 0x0020 = 2^05 = 032
	 */
	public static final Integer TRACKING_TAXI_STATUS_PRINT_STATE_2 = 0X0020;

	/**
	 * 0000.0000.0100.0000 = 0x0040 = 2^06 = 064
	 */
	public static final Integer TRACKING_TAXI_STATUS_IR_BREAK_1 = 0X0040;

	/**
	 * 0000.0000.1000.0000 = 0x0080 = 2^07 = 128
	 */
	public static final Integer TRACKING_TAXI_STATUS_IR_BREAK_2 = 0X0080;

	public static final Integer TAXI_ERR_CODE_STATE = 0x80;

}
