package com.vietek.taxioperation.chat;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Audio;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Vlayout;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.controller.SysUserController;
import com.vietek.taxioperation.controller.UserChatLogController;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.model.UserChatLog;
import com.vietek.taxioperation.mq.ChatMQ;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;

/**
 * 
 * @author Viet Ha Ca
 *
 */
public class ChatWindow extends SelectorComposer<Component> implements EventListener<Event>, Runnable {

	private static final long serialVersionUID = 1L;
	private static String SEND_MESSAGE = "send_msg"; // gui tin nhan
	private static String SEND_REQUEST = "send_request"; // yeu cau hoi diem/goi
															// khach ra xe
	private static String SEND_NOTIFICATION = "send_notification"; // thong bao
																	// online/offline
	private static String ATT_HISTORY_CHAT = "history_chat";
	private static String ATT_HISTORY_MIN_ID = "chatlog_min";

	@Wire
	Div divChat;
	@Wire
	Button btnChat;
	@Wire
	Panel panelChat;

	Panelchildren paneChild;
	Audio audio;
	Hlayout hlChat;
	Div leftDiv;
	Div rightDiv;
	Div divBoard;
	Vbox vbBoard;
	Div divInput;
	Textbox tbMessage;
	Button btnSend;
	Button btnSearch;
	Textbox txtSearch;
	Grid gridUser;
	Rows rows;
	List<UserChatLog> lstChatLogs = null;
	UserChatLogController chatLogController;
	UserChatLog chatLog;
	String sender;
	SysUser currentUser;
	SysUser userReceive;
	int choose = 0;
	SysUserController sysUserCtrl;
	Div divTab;
	ChatWindow chatWindow = this;
	private Row currRow;
	private Row rowFirst;
	private int minID;
	private boolean isSort = false;
	private List<SysUser> listUser;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		return;
	}

	private void initAfterCompose() {
		init();
		try {
			sendNotif(currentUser, true);
			ChatMQ.subcrible(new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					addMsg(event);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	/**
	 * @author Habv Define component in chat window
	 */
	public void init() {
		try {
			panelChat.setTitle("MLG Chat (Hệ thống)");
			panelChat.setMinimizable(true);
			panelChat.addEventListener(Events.ON_MINIMIZE, this);
			btnChat.addEventListener(Events.ON_CLICK, this);

			paneChild = new Panelchildren();
			paneChild.setId("paneChild");
			paneChild.setSclass("paneChild");
			paneChild.setParent(panelChat);

			hlChat = new Hlayout();
			hlChat.setId("hlChat");
			hlChat.setSclass("hlChat");
			hlChat.setStyle("width:100%; height: 100%;");
			hlChat.setParent(paneChild);

			initLeftPanel();
			initRightPanel();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

	}

	@Override
	public void run() {

	}

	/**
	 * @author Habv Set chat window or chat button is visible or hidden
	 */
	public void showhide() {
		if (btnChat.isVisible()) {
			divChat.setWidth("400px");
			divChat.setHeight("300px");
			btnChat.setVisible(false);
			panelChat.setVisible(true);
		} else {
			divChat.setWidth("50px");
			divChat.setHeight("50px");
			btnChat.setVisible(true);
			panelChat.setVisible(false);
		}
	}

	@Override
	public void onEvent(Event event) throws Exception {
		// PanelChat events
		if (event.getTarget() instanceof Panel) {
			if (event.getName().equals(Events.ON_MINIMIZE)) {
				if (panelChat.isMinimized()) {
					showhide();
				}
			}
			if (event.getName().equals(Events.ON_CTRL_KEY)) {
				if (panelChat.isMinimized()) {
					showhide();
				}
			}
		}
		if (event.getTarget() instanceof Button) {
			if (event.getName().equals(Events.ON_CLICK)) {
				showhide();
			}
		}
		if (event.getTarget() instanceof Textbox) {
			if (event.getName().equals(Events.ON_DROP)) {

			}
		}
		if (event.getTarget().equals(tbMessage)) {
			if (event.getName().equals(Events.ON_CHANGING)) {
				if (!isSort) {
					for (Component comp : rows.getChildren()) {
						SysUser user = (SysUser) comp.getAttribute("user");
						if (user != null && userReceive != null) {
							if (user.equals(userReceive)) {
								if (currentUser.getSwitchboard() != null)
									rows.insertBefore(currRow, rows.getChildren().get(2));
								else
									rows.insertBefore(currRow, rows.getChildren().get(1));
								isSort = true;
								break;
							}
						}
					}
				}
			}
			if (event.getName().equals(Events.ON_OK)) {
				if (tbMessage.getValue().trim().isEmpty() || tbMessage.getValue().length() > 1000) {
					return;
				} else
					sendMgs(currentUser, userReceive, tbMessage.getValue(), choose);
			}
		}
	}

	/**
	 * @author Habv Define component to send and show message
	 */
	public void initLeftPanel() {
		leftDiv = new Div();
		leftDiv.setId("leftDiv");
		leftDiv.setSclass("leftDiv");
		leftDiv.setVflex("1");
		leftDiv.setHflex("2");
		leftDiv.setParent(hlChat);

		divBoard = new Div();
		divBoard.setId("divBoard");
		divBoard.setSclass("divBoard");
		divBoard.setParent(leftDiv);

		vbBoard = new Vbox();
		vbBoard.setId("vbBoard");
		vbBoard.setSclass("vbBoard");
		vbBoard.setWidth(divBoard.getWidth());
		vbBoard.setHeight(divBoard.getHeight());
		vbBoard.setParent(divBoard);

		tbMessage = new Textbox();
		tbMessage.setId("tbMessage");
		tbMessage.setSclass("tbMessage");
		tbMessage.setPlaceholder("Nhập tin nhắn tại đây. Ấn Enter để gửi..");
		tbMessage.setParent(leftDiv);
		tbMessage.addEventListener(Events.ON_OK, this);
		tbMessage.addEventListener(Events.ON_CHANGING, this);

		tbMessage.setMaxlength(1000);
	}

	class ChatWindowWorker implements Runnable {

		@Override
		public void run() {
			try {
				initAfterCompose();
			} catch (Exception e) {
				AppLogger.logDebug.error("", e);
			} finally {
				// Executions.deactivate(desktop);
			}
		}

	}

	/**
	 * @author Habv Define component to show list of user in system
	 */
	public void initRightPanel() {
		rightDiv = new Div();
		rightDiv.setId("rightDiv");
		rightDiv.setSclass("rightDiv");
		rightDiv.setVflex("1");
		rightDiv.setHflex("1");
		rightDiv.setParent(hlChat);

		Vlayout vlayout = new Vlayout();
		vlayout.setId("vRight");
		vlayout.setSclass("vRight");
		vlayout.setParent(rightDiv);

		Hlayout hlayout = new Hlayout();
		hlayout.setId("hlSearch");
		hlayout.setSclass("hlSearch");
		hlayout.setParent(vlayout);

		txtSearch = new Textbox();
		txtSearch.setId("tbSearch");
		txtSearch.setSclass("tbSearch");
		txtSearch.setPlaceholder("Tìm kiếm..");
		txtSearch.setParent(hlayout);
		txtSearch.addEventListener(Events.ON_CHANGING, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				filterUser(txtSearch.getValue().trim());
			}
		});
		txtSearch.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				filterUser(txtSearch.getValue().trim());
			}
		});
		txtSearch.addEventListener(Events.ON_OK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				filterUser(txtSearch.getValue().trim());
			}
		});

		btnSearch = new Button();
		btnSearch.setId("btnSearch");
		btnSearch.setSclass("btnSearch");
		btnSearch.setImage("./themes/images/search-icon.gif");
		btnSearch.setParent(hlayout);
		btnSearch.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				filterUser(txtSearch.getValue().trim());
			}
		});

		gridUser = new Grid();
		gridUser.setId("gridUser");
		gridUser.setSclass("gridUser");
		gridUser.setParent(vlayout);

		loadListUser("");
	}

	private void initButtonHistory(final SysUser receiver, int type, Row row) {
		vbBoard.getChildren().clear();
		final int maxId = (int) row.getAttribute(ATT_HISTORY_MIN_ID);
		Button btnHistory = new Button("Lịch sử");
		btnHistory.setStyle(
				"background: transparent; width: 100%; font-style: oblique; font-weight: bold; border: none;");
		btnHistory.setParent(vbBoard);
		btnHistory.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				loadChatLog(receiver, type, maxId);
				row.setAttribute(ATT_HISTORY_MIN_ID, minID);
			}
		});
	}

	/**
	 * @author Habv Get list users
	 * @param value=""
	 *            return all users in system. value=val return list of users has
	 *            name as val
	 * @return list of users
	 */
	private List<SysUser> listUser(String value) {
		if (sysUserCtrl == null)
			sysUserCtrl = (SysUserController) ControllerUtils.getController(SysUserController.class);
		String thisUser = currentUser.getUserName();
		List<SysUser> lstUser = sysUserCtrl.getLstUser(thisUser, value);
		return lstUser;
	}

	/**
	 * @author Habv Show list of users to the right of window chat
	 * @param value
	 */
	public void loadListUser(String value) {
		listUser = listUser(value);
		try {
			if (rows == null) {
				rows = new Rows();
				rows.setId("rowsUsers");
				rows.setSclass("rowsUsers");
				rows.setParent(gridUser);
			}
			rows.getChildren().clear();
			int i = 0;
			List<MessageBox> lstMsg;
			if (listUser.size() == 0) {
				// CHAT TREN TOAN HE THONG
				Div div = new Div();
				div.setSclass("lbName");

				final Label lbName = new Label();
				lbName.setValue("Hệ thống");
				lbName.setParent(div);

				rowFirst = new Row();
				rowFirst.setParent(rows);
				rowFirst.setAttribute("user", null);
				rowFirst.setAttribute("user_name", "Hệ thống");
				rowFirst.setId("all");
				lstMsg = new ArrayList<MessageBox>();
				rowFirst.setAttribute(ATT_HISTORY_CHAT, lstMsg);
				rowFirst.setAttribute(ATT_HISTORY_MIN_ID, -1);
				rowFirst.setSclass("rowUser");
				rowFirst.setStyle("cursor: pointer;");
				rowFirst.appendChild(div);
				rowFirst.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						Clients.evalJavaScript("setVboardWidth()");
						choose = 0;
						userReceive = null;
						currRow = rowFirst;
						isSort = true;
						((HtmlBasedComponent) rowFirst.getChildren().get(0)).setSclass("noMsg");
						panelChat.setTitle("MLG Chat (Hệ thống)");
						tbMessage.setFocus(true);
						initButtonHistory(userReceive, 0, rowFirst);
						@SuppressWarnings("unchecked")
						List<MessageBox> lstMsg = (List<MessageBox>) rowFirst.getAttribute(ATT_HISTORY_CHAT);
						loadHistoryChat(lstMsg);
						// loadChatLog(userReceive, 0);
					}
				});
				// CHAT TREN KENH
				if (currentUser.getSwitchboard() != null) {
					div = new Div();
					div.setSclass("lbName");

					final Label lbName1 = new Label();
					lbName1.setValue("Tổng đài");
					lbName1.setParent(div);

					rowFirst = new Row();
					rowFirst.setParent(rows);
					rowFirst.setAttribute("user", null);
					rowFirst.setAttribute("user_name", "Tổng đài");
					lstMsg = new ArrayList<MessageBox>();
					rowFirst.setAttribute(ATT_HISTORY_CHAT, lstMsg);
					rowFirst.setAttribute(ATT_HISTORY_MIN_ID, 0);
					rowFirst.setId("allSB");
					rowFirst.setSclass("rowUser");
					rowFirst.setStyle("cursor: pointer;");
					rowFirst.appendChild(div);
					rowFirst.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
						@Override
						public void onEvent(Event arg0) throws Exception {
							choose = 1;
							userReceive = null;
							currRow = rowFirst;
							isSort = true;
							((HtmlBasedComponent) rowFirst.getChildren().get(0)).setSclass("noMsg");
							panelChat.setTitle("MLG Chat (Tổng đài)");
							tbMessage.setFocus(true);
							initButtonHistory(userReceive, 1, rowFirst);
							@SuppressWarnings("unchecked")
							List<MessageBox> lstMsg = (List<MessageBox>) rowFirst.getAttribute(ATT_HISTORY_CHAT);
							loadHistoryChat(lstMsg);
							// loadChatLog(userReceive, 1);
							Clients.evalJavaScript("setVboardWidth()");
						}
					});
				}
			} else {
				for (SysUser sysUser : listUser) {
					final SysUser user = sysUser;
					if (i == 0) {
						// CHAT TREN TOAN HE THONG
						Div div = new Div();
						div.setSclass("lbName");
						final Label lbName = new Label();
						lbName.setValue("Hệ thống");
						lbName.setParent(div);

						rowFirst = new Row();
						rowFirst.setParent(rows);
						rowFirst.setAttribute("user", null);
						rowFirst.setAttribute("user_name", "Hệ thống");
						lstMsg = new ArrayList<MessageBox>();
						rowFirst.setAttribute(ATT_HISTORY_CHAT, lstMsg);
						rowFirst.setAttribute(ATT_HISTORY_MIN_ID, 0);
						rowFirst.setId("all");
						rowFirst.setSclass("rowUser");
						rowFirst.setStyle("cursor: pointer;");
						rowFirst.appendChild(div);
						rowFirst.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
							@Override
							public void onEvent(Event arg0) throws Exception {
								Clients.evalJavaScript("setVboardWidth()");
								choose = 0;
								userReceive = null;
								currRow = rowFirst;
								isSort = true;
								((HtmlBasedComponent) rowFirst.getChildren().get(0)).setSclass("noMsg");
								panelChat.setTitle("MLG Chat (Hệ thống)");
								tbMessage.setFocus(true);
								initButtonHistory(userReceive, 0, rowFirst);
								@SuppressWarnings("unchecked")
								List<MessageBox> lstMsg = (List<MessageBox>) rowFirst.getAttribute(ATT_HISTORY_CHAT);
								loadHistoryChat(lstMsg);
								// loadChatLog(userReceive, 0);
							}
						});
						// CHAT TREN KENH
						if (currentUser.getSwitchboard() != null) {
							div = new Div();
							div.setSclass("lbName");

							final Label lbName1 = new Label();
							lbName1.setValue("Tổng đài");
							lbName1.setParent(div);

							rowFirst = new Row();
							rowFirst.setParent(rows);
							rowFirst.setAttribute("user", null);
							rowFirst.setAttribute("user_name", "Tổng đài");
							lstMsg = new ArrayList<MessageBox>();
							rowFirst.setAttribute(ATT_HISTORY_CHAT, lstMsg);
							rowFirst.setAttribute(ATT_HISTORY_MIN_ID, 0);
							rowFirst.setId("allSB");
							rowFirst.setSclass("rowUser");
							rowFirst.setStyle("cursor: pointer;");
							rowFirst.appendChild(div);
							rowFirst.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
								@Override
								public void onEvent(Event arg0) throws Exception {
									choose = 1;
									userReceive = null;
									currRow = rowFirst;
									isSort = true;
									((HtmlBasedComponent) rowFirst.getChildren().get(0)).setSclass("noMsg");
									panelChat.setTitle("MLG Chat (Tổng đài)");
									tbMessage.setFocus(true);
									initButtonHistory(userReceive, 1, rowFirst);
									@SuppressWarnings("unchecked")
									List<MessageBox> lstMsg = (List<MessageBox>) rowFirst
											.getAttribute(ATT_HISTORY_CHAT);
									loadHistoryChat(lstMsg);
									// loadChatLog(userReceive, 1);
									Clients.evalJavaScript("setVboardWidth()");
								}
							});
						}
					}

					i++;
					Hlayout hlayout = new Hlayout();
					hlayout.setSclass("hlName");

					Div div = new Div();
					div.setSclass("divName");
					div.setParent(hlayout);
					final Label lbName = new Label();
					lbName.setSclass("lbName");
					lbName.setValue(sysUser.getName());
					lbName.setParent(div);

					Image imgStatus = new Image();
					imgStatus.setSclass("imgStatus");
					// if (CheckOnlineUtils.isOnline(sysUser))
					// imgStatus.setSrc("./themes/images/chat_visible.png");
					// else
					// imgStatus.setSrc("./themes/images/chat_invisible.png");
					imgStatus.setParent(hlayout);

					final Row row = new Row();
					row.setParent(rows);
					row.setId(sysUser.getId() + "");
					row.setSclass("rowUser");
					row.setAttribute("user", sysUser);
					row.setAttribute("user_name", sysUser.getName());
					lstMsg = new ArrayList<MessageBox>();
					row.setAttribute(ATT_HISTORY_CHAT, lstMsg);
					row.setAttribute(ATT_HISTORY_MIN_ID, -1);
					row.setStyle("cursor: pointer;");
					row.appendChild(hlayout);
					row.setTooltip(sysUser.getName() + "_" + sysUser.getUserName());
					row.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
						@Override
						public void onEvent(Event arg0) throws Exception {
							choose = 2;
							userReceive = user;
							currRow = row;
							isSort = false;
							((HtmlBasedComponent) row.getChildren().get(0)).setSclass("noMsg");
							panelChat.setTitle("MLG Chat (" + user.getName() + ")");
							initButtonHistory(userReceive, 2, row);
							@SuppressWarnings("unchecked")
							List<MessageBox> lstMsg = (List<MessageBox>) row.getAttribute(ATT_HISTORY_CHAT);
							loadHistoryChat(lstMsg);
							// loadChatLog(userReceive, 2);
							Clients.evalJavaScript("setVboardWidth()");
							tbMessage.setFocus(true);
						}
					});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	private void filterUser(String name) throws UnsupportedEncodingException {
		if (name.length() == 0) {
			for (Component comp : rows.getChildren()) {
				comp.setVisible(true);
			}
		} else {
			byte[] byteName = name.getBytes("UTF8");
			String iname = new String(byteName, "UTF8");
			for (Component comp : rows.getChildren()) {
				byte[] un = comp.getAttribute("user_name").toString().toLowerCase().getBytes("UTF8");
				String rowUser = new String(un, "UTF8");
				// if(comp.getAttribute("user_name").toString().toLowerCase().contains(name.toLowerCase())){
				if (rowUser.contains(iname)) {
					comp.setVisible(true);
				} else {
					comp.setVisible(false);
				}
			}
		}
	}

	/**
	 * @author Habv List of users that have same switchboard with user
	 * @param user
	 *            - current user
	 * @return
	 */
	public List<SysUser> listUserSB(SysUser user) {
		if (sysUserCtrl == null)
			sysUserCtrl = (SysUserController) ControllerUtils.getController(SysUserController.class);
		return sysUserCtrl.find("from SysUser where switchboard=?", user.getSwitchboard());
	}

	/**
	 * @author Habv Get chat history in database
	 * @param receiver
	 *            - user who current user want to send message
	 * @param type
	 *            - type of message (message or request)
	 * @return list of history chat
	 */
	private List<UserChatLog> getChatLog(SysUser receiver, int type, int maxID) {
		List<UserChatLog> lstLogs = new ArrayList<UserChatLog>();
		if (chatLogController == null)
			chatLogController = (UserChatLogController) ControllerUtils.getController(UserChatLogController.class);
		if (receiver == null) {
			// HE THONG VA TONG DAI
			if (type == 0 || type == 1)
				if (type == 0) {
					if (maxID < 0) {
						lstLogs = chatLogController.find(10,
								"from UserChatLog where receiver is null and type=?  ORDER BY dateCreate DESC", type);
					} else {
						lstLogs = chatLogController.find(10,
								"from UserChatLog where receiver is null and type=? and id<?  ORDER BY dateCreate DESC",
								type, maxID);
					}
				} else {
					if (maxID < 0) {
						lstLogs = chatLogController.find(10,
								"from UserChatLog where receiver is null and sender.switchboard=? and type=?  ORDER BY dateCreate DESC",
								currentUser.getSwitchboard(), type);
					} else {
						lstLogs = chatLogController.find(10,
								"from UserChatLog where receiver is null and sender.switchboard=? and type=? and id<?  ORDER BY dateCreate DESC",
								currentUser.getSwitchboard(), type, maxID);
					}
				}

		}
		// Chat rieng
		else {
			if (maxID < 0) {
				lstLogs = chatLogController.find(10,
						"from UserChatLog where (sender=? and receiver=?) or (sender=? and receiver=?) ORDER BY dateCreate DESC",
						currentUser, receiver, receiver, currentUser);
			} else {
				lstLogs = chatLogController.find(10,
						"from UserChatLog where id<? AND (sender=? and receiver=?) or (sender=? and receiver=?) ORDER BY dateCreate DESC",
						maxID, currentUser, receiver, receiver, currentUser);
			}
		}
		return lstLogs;
	}

	/**
	 * @author Habv Show history chat to window
	 * @param receiver
	 *            - user who current user want to send message
	 * @param type
	 *            - type of message (message or request)
	 */
	private void loadChatLog(SysUser receiver, int type, int maxID) {
		Clients.showBusy(vbBoard, "Đang tải dữ liệu ...");
		lstChatLogs = getChatLog(receiver, type, maxID);
		if (lstChatLogs.size() > 0) {
			UserChatLog userChatLog;
			MessageBox msgBox;
			try {
				for (int i = 0; i < lstChatLogs.size(); i++) {
					userChatLog = lstChatLogs.get(i);
					if (i == 0)
						minID = userChatLog.getId();
					if (userChatLog.getType() == 3) {
						String msg = userChatLog.getMessage().substring(0, userChatLog.getMessage().lastIndexOf("("));
						String strId = userChatLog.getMessage().substring(userChatLog.getMessage().lastIndexOf("(") + 1,
								userChatLog.getMessage().lastIndexOf(")"));
						msgBox = new MessageBox(userChatLog.getSender().getName(), msg,
								userChatLog.getDateCreate().getTime(), true, Integer.parseInt(strId));
					} else {
						msgBox = new MessageBox(userChatLog.getSender().getName(), userChatLog.getMessage(),
								userChatLog.getDateCreate().getTime(), false, -1);
					}
					if (vbBoard.getChildren().size() > 2)
						vbBoard.insertBefore(msgBox, vbBoard.getChildren().get(1));
					else
						vbBoard.appendChild(msgBox);
					Component lastComp = vbBoard.getChildren().get(vbBoard.getChildren().size() - 1);
					Clients.scrollIntoView(lastComp);
				}
				// for (int i = (lstChatLogs.size() - 1); i >= 0; i--) {
				// userChatLog = lstChatLogs.get(i);
				// if (userChatLog.getType() == 3) {
				// String msg = userChatLog.getMessage().substring(0,
				// userChatLog.getMessage().lastIndexOf("("));
				// String strId =
				// userChatLog.getMessage().substring(userChatLog.getMessage().lastIndexOf("(")
				// + 1,
				// userChatLog.getMessage().lastIndexOf(")"));
				// msgBox = new MessageBox(userChatLog.getSender().getName(),
				// msg,
				// userChatLog.getDateCreate().getTime(), true,
				// Integer.parseInt(strId));
				// } else {
				// msgBox = new MessageBox(userChatLog.getSender().getName(),
				// userChatLog.getMessage(),
				// userChatLog.getDateCreate().getTime(), false, -1);
				// }
				// msgBox.setParent(vbBoard);
				//
				// }
			} catch (Exception e) {
				AppLogger.logDebug.error("Loi chat: " + e.getMessage(), e);
			} finally {
				// Executions.deactivate(desktop);
			}
		}
		Clients.clearBusy(vbBoard);
	}

	/**
	 * @author Habv Define row display user in grid on right window chat
	 * @param user
	 *            - user's displayed
	 * @param isOnline
	 *            - user online or offline
	 * @return Halyout
	 */
	public Hlayout hlUser(SysUser user, boolean isOnline) {
		Hlayout hlayout = new Hlayout();
		hlayout.setSclass("hlNameNew");

		Div div = new Div();
		div.setSclass("divName");
		div.setParent(hlayout);
		final Label lbName = new Label();
		lbName.setSclass("lbName");
		lbName.setValue(user.getName());
		lbName.setParent(div);

		Image imgStatus = new Image();
		imgStatus.setSclass("imgStatus");
		if (isOnline)
			imgStatus.setSrc("./themes/images/chat_visible.png");
		else
			imgStatus.setSrc("./themes/images/chat_invisible.png");
		imgStatus.setParent(hlayout);
		return hlayout;
	}

	private void loadHistoryChat(List<MessageBox> lstMsg) {
		if (lstMsg.size() > 0) {
			MessageBox msgBox;
			for (int i = 0; i < lstMsg.size(); i++) {
				msgBox = lstMsg.get(i);
				msgBox.setParent(vbBoard);
			}
		}
	}

	/**
	 * @author Habv Send message to other
	 * @param sender
	 *            - user send message
	 * @param receiver
	 *            - user receives message
	 * @param message
	 *            - content of message
	 * @param type
	 *            - type of message
	 */
	private void sendMgs(SysUser sender, SysUser receiver, String message, int type) {
		// Show message
		try {
			MessageBox msgBox = new MessageBox(sender.getName(), message, System.currentTimeMillis(), false, -1);
			msgBox.setParent(vbBoard);
			tbMessage.setRawValue("");
			Clients.scrollIntoView(vbBoard.getChildren().get(vbBoard.getChildren().size() - 1));
			// Luu tin nhan vao memory
			@SuppressWarnings("unchecked")
			List<MessageBox> lstMsg = (List<MessageBox>) currRow.getAttribute(ATT_HISTORY_CHAT);
			lstMsg.add(msgBox);
			currRow.setAttribute(ATT_HISTORY_CHAT, lstMsg);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Executions.deactivate(desktop);
		}

		// Save message
		chatLog = new UserChatLog();
		chatLog.setSender(sender);
		chatLog.setReceiver(receiver);
		chatLog.setDateCreate(new Timestamp(System.currentTimeMillis()));
		chatLog.setMessage(message);
		chatLog.setType(type);
		chatLog.save();
		// Send message
		Message msg = new Message(sender, receiver, message, System.currentTimeMillis(), type);
		ChatMQ.publish(new Event(SEND_MESSAGE, null, msg));

	}

	/**
	 * @author Habv Send request to other
	 * @param receiver
	 *            - user receives request
	 * @param message
	 *            - content of request and request id - format: content +
	 *            (requestID)
	 */
	static public void sendRequest(SysUser receiver, String message) {
		// Save message
		UserChatLogController chatLogController = (UserChatLogController) ControllerUtils
				.getController(UserChatLogController.class);
		UserChatLog chatLog = new UserChatLog();
		chatLog.setSender(Env.getUser());
		chatLog.setReceiver(receiver);
		chatLog.setDateCreate(new Timestamp(System.currentTimeMillis()));
		chatLog.setMessage(message);
		chatLog.setType(3);
		chatLogController.save(chatLog);
		// Send request
		// if (!CheckOnlineUtils.isOnline(receiver)) {
		// Clients.showNotification("Chưa gửi được yêu cầu. Người nhận không
		// online.", "info", null, "bottom_right",
		// 4000);
		// return;
		// }
		Message msg = new Message(Env.getUser(), receiver, message, System.currentTimeMillis(), 3);
		ChatMQ.publish(new Event(SEND_REQUEST, null, msg));
	}

	/**
	 * @author Habv Send notification that current user online or offline to
	 *         other
	 * @param user
	 *            - current user
	 * @param isOnline
	 *            - true or false
	 */
	static public void sendNotif(SysUser user, boolean isOnline) {
		Message msg = new Message(user, isOnline);
		ChatMQ.publish(new Event(SEND_NOTIFICATION, null, msg));
	}

	/**
	 * @author Habv Receive message and analyze it that's a message,
	 *         notification or request
	 * @param event
	 *            - event send message
	 */
	public void addMsg(Event event) {
		Message msg = (Message) event.getData();
		try {
			// Tuanpa
			if (!Env.getHomePage().getDesktop().isAlive() || this.getPage() == null) {
				ChatMQ.unsubcrible(this);
				return;
			}

			if (event.getName().equalsIgnoreCase(SEND_MESSAGE)) { // tin nhan
				MessageBox msgBox = new MessageBox(msg.getSender().getUserName(), msg.getMessage(),
						msg.getTime().getTime(), false, -1);
				if (msg.getReceiver() == null) {// tin nhan tong dai/ he thong
					if (!currentUser.equals(msg.getSender())) { // khong phai
																// nguoi gui
						if (msg.getType() == 0) { // tin nhan he thong
							if (choose == 0) { // dang chat he thong
								msgBox.setParent(vbBoard);
								Clients.scrollIntoView(vbBoard.getChildren().get(vbBoard.getChildren().size() - 1));
								if (!panelChat.isVisible()) {
									Clients.showNotification(
											msg.getSender().getName() + " gửi 1 tin nhắn trên hệ thống!", "info", null,
											"bottom_right", 4000);
								}
							} else { // dang chat tong dai/ chat rieng
								Clients.showNotification(msg.getSender().getName() + " gửi 1 tin nhắn trên hệ thống!",
										"info", null, "bottom_right", 4000);
								for (Component comp : rows.getChildren()) {
									if (comp.getId().equalsIgnoreCase("all")) {
										((HtmlBasedComponent) comp.getChildren().get(0)).setSclass("newMsg");
										break;
									}
								}
							}
							// Luu tin nhan vao list memory
							for (Component comp : rows.getChildren()) {
								if (comp.getId().equalsIgnoreCase("all")) {
									@SuppressWarnings("unchecked")
									List<MessageBox> lstMsg = (List<MessageBox>) comp.getAttribute(ATT_HISTORY_CHAT);
									lstMsg.add(msgBox);
									comp.setAttribute(ATT_HISTORY_CHAT, lstMsg);
									break;
								}
							}
						} else { // tin nhan tong dai
							if (currentUser.getSwitchboard() != null
									&& currentUser.getSwitchboard().equals(msg.getSender().getSwitchboard())) {
								if (choose == 1) { // dang chat tong dai
									if (panelChat.isVisible()) {
										msgBox.setParent(vbBoard);
										Clients.scrollIntoView(
												vbBoard.getChildren().get(vbBoard.getChildren().size() - 1));
									}
								} else { // dang chat he thong/ chat rieng
									Clients.showNotification(
											msg.getSender().getName() + " gửi 1 tin nhắn trên tổng đài!", "info", null,
											"bottom_right", 4000);
									for (Component comp : rows.getChildren()) {
										if (comp.getId().equalsIgnoreCase("allSB")) {
											((HtmlBasedComponent) comp.getChildren().get(0)).setSclass("newMsg");
											break;
										}
									}
								}
							}
							// Luu tin nhan vao memory
							for (Component comp : rows.getChildren()) {
								if (comp.getId().equalsIgnoreCase("allSB")) {
									@SuppressWarnings("unchecked")
									List<MessageBox> lstMsg = (List<MessageBox>) comp.getAttribute(ATT_HISTORY_CHAT);
									lstMsg.add(msgBox);
									comp.setAttribute(ATT_HISTORY_CHAT, lstMsg);
									break;
								}
							}
						}
					}

				} else { // tin nhan rieng
					if (currentUser.equals(msg.getReceiver())) {
						if (userReceive == null) { // dang chat tong dai/ he
													// thong
							Clients.showNotification(msg.getSender().getName() + " gửi 1 tin nhắn cho bạn!", "info",
									null, "bottom_right", 4000);
							for (Component comp : rows.getChildren()) {
								if (comp.getId().equalsIgnoreCase(String.valueOf(msg.getSender().getId()))) {
									((HtmlBasedComponent) comp.getChildren().get(0)).setSclass("newMsg");
									break;
								}
							}
						} else { // dang chat rieng
							if (userReceive.getId() != msg.getSender().getId()) { // dang
																					// chat
																					// voi
																					// nguoi
																					// khac
								Clients.showNotification(msg.getSender().getName() + " gửi 1 tin nhắn cho bạn!", "info",
										null, "bottom_right", 4000);
								for (Component comp : rows.getChildren()) {
									if (comp.getId().equalsIgnoreCase(String.valueOf(msg.getSender().getId()))) {
										((HtmlBasedComponent) comp.getChildren().get(0)).setSclass("newMsg");
										break;
									}
								}
							} else { // dang chat voi nguoi gui
								msgBox.setParent(vbBoard);
								Clients.scrollIntoView(vbBoard.getChildren().get(vbBoard.getChildren().size() - 1));
								if (!panelChat.isVisible())
									Clients.showNotification(msg.getSender().getName() + " gửi 1 tin nhắn cho bạn!",
											"info", null, "bottom_right", 4000);
							}
						}
						// Day nguoi dung len dau danh sach va Luu lai tin nhan
						for (Component comp : rows.getChildren()) {
							SysUser user = (SysUser) comp.getAttribute("user");
							if (user != null) {
								if (user.equals(msg.getSender())) {
									Row rowSender = (Row) comp;
									if (currentUser.getSwitchboard() != null)
										rows.insertBefore(rowSender, rows.getChildren().get(2));
									else
										rows.insertBefore(rowSender, rows.getChildren().get(1));
									// Luu tin nhan vao memory
									@SuppressWarnings("unchecked")
									List<MessageBox> lstMsg = (List<MessageBox>) comp.getAttribute(ATT_HISTORY_CHAT);
									lstMsg.add(msgBox);
									comp.setAttribute(ATT_HISTORY_CHAT, lstMsg);
									break;
								}
							}
						}
					}
				}
			} else if (event.getName().equalsIgnoreCase(SEND_NOTIFICATION)) { // thong
																				// bao
																				// online/offline
				for (Component comp : rows.getChildren()) {
					if (comp.getId().equalsIgnoreCase(String.valueOf(msg.getSender().getId()))) {
						comp.getChildren().clear();
						comp.appendChild(hlUser(msg.getSender(), msg.getIsOnline()));
					}
				}
			} else if (event.getName().equalsIgnoreCase(SEND_REQUEST)) { // yeu
																			// cau
																			// hoi
																			// diem
																			// goi
																			// khach
																			// ra
																			// xe
				String message = msg.getMessage().substring(0, msg.getMessage().lastIndexOf("("));
				String idRequest = msg.getMessage().substring(msg.getMessage().lastIndexOf("(") + 1,
						msg.getMessage().lastIndexOf(")"));
				int request = Integer.parseInt(idRequest);
				if (currentUser.equals(msg.getReceiver())) { // nguoi nhan
					// audio.play();
					Clients.showNotification("Bạn nhận được 1 yêu cầu", "info", null, "bottom_right", 4000);
					divChat.setWidth("400px");
					divChat.setHeight("300px");
					if (userReceive == null || !userReceive.equals(msg.getSender()))
						vbBoard.getChildren().clear();
					choose = 2;
					userReceive = msg.getSender();
					btnChat.setVisible(false);
					panelChat.setTitle("MLG Chat (" + msg.getSender().getName() + ")");
					panelChat.setVisible(true);
					MessageBox msgBox = new MessageBox(msg.getSender().getUserName(), message, msg.getTime().getTime(),
							true, request);
					msgBox.setParent(vbBoard);
					Clients.scrollIntoView(vbBoard.getChildren().get(vbBoard.getChildren().size() - 1));
					// Luu vao memory
					for (Component comp : rows.getChildren()) {
						SysUser user = (SysUser) comp.getAttribute("user");
						if (user != null) {
							if (user.equals(msg.getSender())) {
								Row rowSender = (Row) comp;
								if (currentUser.getSwitchboard() != null)
									rows.insertBefore(rowSender, rows.getChildren().get(2));
								else
									rows.insertBefore(rowSender, rows.getChildren().get(1));
								// Luu tin nhan vao memory
								@SuppressWarnings("unchecked")
								List<MessageBox> lstMsg = (List<MessageBox>) comp.getAttribute(ATT_HISTORY_CHAT);
								lstMsg.add(msgBox);
								comp.setAttribute(ATT_HISTORY_CHAT, lstMsg);
								break;
							}
						}
					}
				} else if (currentUser.equals(msg.getSender())) { // nguoi gui
					Clients.showNotification("Đã gửi yêu cầu", "info", null, "bottom_right", 4000);
					if (userReceive != null && userReceive.equals(msg.getReceiver())) {
						MessageBox msgBox = new MessageBox(msg.getSender().getUserName(), message,
								msg.getTime().getTime(), true, request);
						msgBox.setParent(vbBoard);
						Clients.scrollIntoView(vbBoard.getChildren().get(vbBoard.getChildren().size() - 1));
						// Day nguoi nhan len dau va luu tin nhan
						for (Component comp : rows.getChildren()) {
							SysUser user = (SysUser) comp.getAttribute("user");
							if (user != null) {
								if (user.equals(msg.getReceiver())) {
									Row rowSender = (Row) comp;
									if (currentUser.getSwitchboard() != null)
										rows.insertBefore(rowSender, rows.getChildren().get(2));
									else
										rows.insertBefore(rowSender, rows.getChildren().get(1));
									// Luu tin nhan vao memory
									@SuppressWarnings("unchecked")
									List<MessageBox> lstMsg = (List<MessageBox>) comp.getAttribute(ATT_HISTORY_CHAT);
									lstMsg.add(msgBox);
									comp.setAttribute(ATT_HISTORY_CHAT, lstMsg);
									break;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("Loi chat: " + e.getMessage(), e);
		} finally {
		}

	}
}
