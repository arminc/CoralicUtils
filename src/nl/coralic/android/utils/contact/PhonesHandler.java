/**
 * 
 */
package nl.coralic.android.utils.contact;

import nl.coralic.android.utils.constants.Const;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.Contacts.People.Phones;
import android.util.Log;

/**
 * @author "Armin Čoralić"
 */
public class PhonesHandler
{
	public PhoneNumbers getPhoneNumbersForSelectedContact(ContentResolver contentResolver, Uri contactUri)
	{
		PhoneNumbers phones = new PhoneNumbers();
		Log.d(Const.TAG_CONH, "contactUri: " + contactUri);
		Uri numbers_uri = Uri.withAppendedPath(contactUri, Contacts.People.Phones.CONTENT_DIRECTORY);

		Cursor c = contentResolver.query(numbers_uri, null, null, null, Phones.DEFAULT_SORT_ORDER);
		if (c.moveToFirst())
		{
			while (!c.isAfterLast())
			{
				phones.addPhoneNumber(c.getString(c.getColumnIndex(Contacts.Phones.NUMBER)), getType(c));
				c.moveToNext();
			}
		}
		return phones;
	}

	private String getType(Cursor c)
	{
		int type = c.getInt(c.getColumnIndex(Contacts.Phones.TYPE));

		if (type == Contacts.Phones.TYPE_CUSTOM)
		{
			return c.getString(c.getColumnIndex(Contacts.Phones.LABEL));
		}
		if (type == Contacts.Phones.TYPE_HOME)
		{
			return Const.PHONE_TYPE_HOME;
		}
		if (type == Contacts.Phones.TYPE_MOBILE)
		{
			return Const.PHONE_TYPE_MOBILE;
		}
		else
		{
			return Const.PHONE_TYPE_OTHER;
		}
	}
}
