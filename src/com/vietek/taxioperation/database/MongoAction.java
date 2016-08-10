package com.vietek.taxioperation.database;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import org.bson.Document;
import org.bson.types.Binary;
import org.zkoss.zk.ui.Component;

import com.google.maps.model.LatLng;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.vietek.taxioperation.ui.controller.vmap.VMarker;
import com.vietek.taxioperation.ui.controller.vmap.VPolyline;
import com.vietek.taxioperation.util.MapUtils;
import com.vietek.tracking.ui.model.GpsTrackingMsg;
import com.vietek.tracking.ui.model.VehicleMarker;

public class MongoAction {

	MongoDatabase mdb = MongoAccess.getDB("txm_tracking");

	public static final long DELTA_GMT = 7 * 60 * 60 * 1000;

	private void __parse_packet_gps(byte[] data, int offset, GpsTrackingMsg packet) throws IOException {
		InputStream is = new ByteArrayInputStream(data);
		__parse_packet_gps_from_stream(is, offset, packet);
	}

	private void __parse_packet_gps_from_stream(InputStream is, int offset, GpsTrackingMsg packet) throws IOException {

		DataInputStream dis = new DataInputStream(is);
		byte[] buf = new byte[10];

		/* skip offset */
		dis.skip(offset);
		/* time */
		dis.read(buf, 0, 4);
		long rawTime = __read_uint32(buf, 0);
		packet.setRawTime(rawTime);
		Date dt = new Date(rawTime * 1000 - DELTA_GMT);
		packet.setTimeLog(dt);
		/* segment index */
		dis.read(buf, 0, 2);
		packet.setIndexMsg(__read_uint16(buf, 0) & 0x3fff);
		/* d_index */
		dis.read(buf, 0, 1);
		packet.setIndexDriver((int) buf[0]);
		/* ADC1 */
		dis.read(buf, 0, 2);
		packet.setAdc1(__read_uint16(buf, 0));
		/* ADC2 */
		dis.read(buf, 0, 2);
		packet.setAdc2(__read_uint16(buf, 0));
		/* ADC3 */
		dis.read(buf, 0, 2);
		packet.setAdc3(__read_uint16(buf, 0));
		/* Status */
		dis.read(buf, 0, 2);
		packet.setDigitalStatus(__read_uint16(buf, 0));
		/* longitude */
		dis.read(buf, 0, 4);
		packet.setLongitude(__read_float32(buf, 0));
		/* latitude */
		dis.read(buf, 0, 4);
		packet.setLatitude(__read_float32(buf, 0));
		/* gps speed */
		dis.read(buf, 0, 10);
		packet.setGpsSepeed(__copy_byte_array(buf, 0, 10));
		/* speed */
		dis.read(buf, 0, 1);
		packet.setMeterSpeed(__read_uint8(buf, 0));
		/* dx */
		dis.read(buf, 0, 9);
		packet.setLatDiff(__copy_byte_array(buf, 0, 9));
		/* dy */
		dis.read(buf, 0, 9);
		packet.setLonDiff(__copy_byte_array(buf, 0, 9));
		/* fuel */
		dis.read(buf, 0, 2);
		packet.setSulInfo(__read_uint16(buf, 0));
		/* gsm signal */
		dis.read(buf, 0, 1);
		byte[] data = new byte[1];
		System.arraycopy(buf, 0, data, 0, 1);
		// int gms = __read_int8(buf, 0);
		int gms = byte2UInt8(data);
		// packet.setGsmIntensity(IntegerAppUtils.byte2UInt8(data));
		packet.setGsmIntensity(gms);
		/* gps signal */
		dis.read(buf, 0, 1);
		data = new byte[1];
		System.arraycopy(buf, 0, data, 0, 1);
		packet.setGpsIntensity(byte2UInt8(data));
		/* battery voltage */
		dis.read(buf, 0, 2);
		packet.setPinPower(__read_uint16(buf, 0));
		/* system voltage */
		dis.read(buf, 0, 2);
		packet.setAcquiPower(__read_uint16(buf, 0));
		/* PathPerDay */
		dis.read(buf, 0, 2);
		packet.setPathPerDay(__read_uint16(buf, 0));
		/* working_time */
		dis.read(buf, 0, 2);
		packet.setTimeDrivingPerDay(__read_uint16(buf, 0));
		/* working_time_cons */
		dis.read(buf, 0, 2);
		packet.setTimeDrivingContinuous(__read_uint16(buf, 0));
		/* taxi status */
		dis.read(buf, 0, 1);
		packet.setStatusTaxi(__read_uint8(buf, 0));
		/* totalMoney */
		dis.read(buf, 0, 4);
		packet.setTotalMoney(__read_uint32(buf, 0));
		/* totalMoneyShift */
		dis.read(buf, 0, 2);
		packet.setTotalMoneyShift(__read_uint16(buf, 0));
		/* emptyPath */
		dis.read(buf, 0, 2);
		packet.setEmptyPath(__read_uint16(buf, 0));
		/* pathTrip */
		dis.read(buf, 0, 2);
		packet.setPathTrip(__read_uint16(buf, 0));
		/* totalTrip */
		dis.read(buf, 0, 1);
		packet.setTotalTrip(__read_uint8(buf, 0));
		/* moneyTrip */
		dis.read(buf, 0, 2);
		packet.setMoneyTrip(__read_uint16(buf, 0));
		/* tripPath */
		dis.read(buf, 0, 2);
		packet.setTripPath(__read_uint16(buf, 0));
	}

	static private long __read_uint32(byte[] data, int offset) {
		byte[] buf = new byte[8];
		byte[] zero = { 0, 0, 0, 0 };
		System.arraycopy(data, offset, buf, 0, 4);
		System.arraycopy(zero, 0, buf, 4, 4);
		return ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN).getLong();
	}

	static private int __read_uint16(byte[] data, int offset) {
		byte[] buf = { data[offset], data[offset + 1], 0, 0 };
		return ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}

	static private float __read_float32(byte[] data, int offset) {
		return ByteBuffer.wrap(data, offset, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
	}

	static private int[] __copy_byte_array(byte[] data, int offset, int length) {
		int[] buf = new int[length];
		for (int i = 0; i < length; i++) {
			if (data[i] < 0) {
				buf[i] = 256 - data[i];
			} else
				buf[i] = (int) data[i];
		}
		return buf;
	}

	static int __read_int8(byte[] data, int offset) {
		byte[] buf = new byte[4];

		if ((data[offset] & 0x80) != 0)
			buf[1] = buf[2] = buf[3] = (byte) 0xff;
		else
			buf[1] = buf[2] = buf[3] = 0;
		buf[0] = data[offset];
		return ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}

	static private int __read_uint8(byte[] data, int offset) {
		byte[] buf = { data[offset], 0, 0, 0 };
		return ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}

	public static int byte2UInt8(byte[] data) {
		int value;
		if (data[0] < 0) {
			value = 256 + data[0];
		} else {
			value = data[0];
		}
		return value;
	}

	public List<GpsTrackingMsg> getHistoryVehicle(long fromdate, long todate, int deviceid) {
		List<GpsTrackingMsg> listresult = new ArrayList<GpsTrackingMsg>();
		try {
			if (mdb == null) {
				mdb = MongoAccess.getDB("txm_tracking");
			}
			MongoCollection<Document> coll = mdb.getCollection("device" + deviceid);
			DBObject condition = new BasicDBObject(2);
			condition.put("$gt", fromdate - TimeUnit.MINUTES.toSeconds(5));
			condition.put("$lt", todate + TimeUnit.MINUTES.toSeconds(5));
			FindIterable<Document> iterable = coll.find(new BasicDBObject("DateLogInSecond", condition))
					.sort(new Document("DateLogInSecond", 1));

			iterable.forEach(new Block<Document>() {
				@Override
				public void apply(Document arg0) {
					GpsTrackingMsg history = new GpsTrackingMsg();
					Binary bs = (Binary) arg0.get("DetailData");
					try {
						__parse_packet_gps(bs.getData(), 0, history);
						long rctime = (Long) arg0.get("ReceiveDate");
						String rowid = arg0.getObjectId("_id").toString();
						history.setRowid(rowid);
						history.setReceivetime(new Date(rctime * 1000));
					} catch (IOException e) {
						e.printStackTrace();
					}
					listresult.add(history);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listresult;

	}

	public List<Component> getViewHistory(long utcfromtime, long utctotime, int deviceid) {
		long fromtime = (utcfromtime + TimeUnit.HOURS.toMillis(7)) / 1000;
		long totime = (utctotime + TimeUnit.HOURS.toMillis(7)) / 1000;
		List<Component> lstResult = new ArrayList<>();
		List<GpsTrackingMsg> trackings = getHistoryVehicle(fromtime, totime, deviceid);
		if (trackings != null && trackings.size() > 0) {
			List<LatLng> path = new ArrayList<LatLng>();
			int state = trackings.get(0).getInTrip();
			GpsTrackingMsg pointStop = null;
			long timePrevious = 0;
			ListIterator<GpsTrackingMsg> ite = trackings.listIterator();
			while (ite.hasNext()) {
				GpsTrackingMsg gpselement = ite.next();
				if (!gpselement.isLostGPS()) {
					if ((gpselement.getInTrip()) == state) {
						if (path.size() > 0) {
							LatLng pointLast = path.get(path.size() - 1);
							double distance = MapUtils.distance(pointLast.lng, pointLast.lat, gpselement.getLongitude(),
									gpselement.getLatitude());
							if (timePrevious > 0) {
								long deltatime = gpselement.getTimeLog().getTime() - timePrevious;
								if (deltatime < TimeUnit.MINUTES.toMillis(5)) {
									if (distance > 10 || gpselement.isPointSpecial()) {
										path.add(gpselement.getLatlng());
									}
								} else {
									lstResult.add(setupVpolyline(path, state));
									path = new ArrayList<LatLng>();
									path.add(gpselement.getLatlng());
									state = gpselement.getInTrip();
								}
							}
							timePrevious = gpselement.getTimeLog().getTime();

						} else {
							path.add(gpselement.getLatlng());
						}

					} else {

						if (state == GpsTrackingMsg.KHONG_KHACH) {
							setupVmarker(gpselement, VehicleMarker.TYPE_BEGIN_TRIP);
							gpselement.setStartTrip(true);
						}
						path.add(gpselement.getLatlng());
						lstResult.add(setupVpolyline(path, state));
						path = new ArrayList<LatLng>();
						path.add(gpselement.getLatlng());
						state = gpselement.getInTrip();
					}
					if (gpselement.isStopPoint()) {
						pointStop = gpselement;
					} else if (gpselement.isActivityPoint()) {
						if (pointStop != null) {
							pointStop.setTimestop(gpselement.getMsgOld().getTimestop());
							lstResult.add(setupVmarker(pointStop, VehicleMarker.TYPE_POINT_STOP));
							pointStop = null;
						}
						lstResult.add(setupVmarker(gpselement, VehicleMarker.TYPE_POINT_START));
					}
				} else {
					ite.remove();
					path = new ArrayList<LatLng>();
				}
			}

		}
		return lstResult;
	}

	public VPolyline setupVpolyline(List<LatLng> lstpath, int linestate) {
		VPolyline gpline = new VPolyline();
		gpline.setSclass("z-polyline-history");
		gpline.setWidth("2px");
		gpline.setPath((ArrayList<LatLng>) lstpath);
		if (linestate == GpsTrackingMsg.CO_KHACH) {
			gpline.setColor("#009900");
		} else if (linestate == GpsTrackingMsg.KHONG_KHACH) {
			gpline.setColor("#fc060c");
		}
		return gpline;
	}

	public VMarker setupVmarker(GpsTrackingMsg data, int typemarker) {
		VehicleMarker marker = new VehicleMarker(data, typemarker);
		return marker;
	}

}