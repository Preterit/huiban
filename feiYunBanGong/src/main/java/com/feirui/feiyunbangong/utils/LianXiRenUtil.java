package com.feirui.feiyunbangong.utils;

import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;

public class LianXiRenUtil {

	public static String[] readConnect(Activity activity) {
		try {
			StringBuffer sb_number = new StringBuffer();
			StringBuffer sb_name = new StringBuffer();// 姓名拼接：
			Cursor cursor = activity
					.getBaseContext()
					.getContentResolver()
					.query(ContactsContract.Contacts.CONTENT_URI, null, null,
							null, null);
			int contactIdIndex = 0;
			int nameIndex = 0;
			if (cursor.getCount() > 0) {
				contactIdIndex = cursor
						.getColumnIndex(ContactsContract.Contacts._ID);
				nameIndex = cursor
						.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			}
			while (cursor.moveToNext()) {
				String contactId = cursor.getString(contactIdIndex);
				String name = cursor.getString(nameIndex);//获取联系人名字
				/*
				 * 查找该联系人的phone信息
				 */
				Cursor phones = activity
						.getBaseContext()
						.getContentResolver()
						.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
								null,
								ContactsContract.CommonDataKinds.Phone.CONTACT_ID
										+ "=" + contactId, null, null);
				int phoneIndex = 0;
				if (phones.getCount() > 0) {
					phoneIndex = phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
				}
				while (phones.moveToNext()) {
					String phoneNumber = phones.getString(phoneIndex);

					// 处理一下，将空格去除：
					phoneNumber = phoneNumber.replaceAll(" ", "");

					// 判断是不是手机号：
					if (Utils.isPhone(phoneNumber)) {

						sb_number.append(phoneNumber);
						sb_number.append(",");
						sb_name.append(name);
						sb_name.append(",");

					}

				}
				phones.close();
			}
			cursor.close();
			sb_number.delete(sb_number.length() - 1, sb_number.length());
			sb_name.delete(sb_name.length() - 1, sb_name.length());

			String[] str = new String[2];
			str[0] = sb_name.toString();
			str[1] = sb_number.toString();

			return str;

		} catch (Exception e) {
			return null;
		}

	}

}
