package hg.zj.contactssidebar;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.ArrayList;
import java.util.List;

public class ContactTest {
    public ContactTest(Context context) {
        this.context = context;
    }

    Context context;

    public List<Contact> getAllContact() {
        List<Contact> listMembers = new ArrayList<>();
        String[] projection = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(uri, projection,
                null, null, ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY);
        try {

            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    String name = cursor.getString(0);
                    String sortKey = getSortKey(Pinyin.toPinyin(name.charAt(0)));
                    contact.setName(name);
                    contact.setIndex(sortKey);
                    if (name != null)
                        listMembers.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return listMembers;
    }

    /**
     * 获取sort key的首个字符，如果是英文字母就直接返回，否则返回#。
     *
     * @param sortKeyString 数据库中读取出的sort key
     * @return 英文字母或者#
     */
    private static String getSortKey(String sortKeyString) {
        String key = sortKeyString.substring(0, 1).toUpperCase();
        if (key.matches("[A-Z]")) {
            return key;
        }
        return "#";
    }
}
