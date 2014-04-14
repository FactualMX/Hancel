package org.hansel.myAlert;

import java.util.ArrayList;

public class ContactInfo {
	public ContactInfo(String displayName, String primaryPhoneNumber, String _id) {
		DisplayName = displayName;
		this.primaryPhoneNumber = primaryPhoneNumber;
		this._id = _id;
		phoneNumbers = new ArrayList<String>();
	}
	/**
	 * @return the contactEmail
	 */
	public ArrayList<String> getContactEmail() {
		return contactEmail;
	}
	/**
	 * @param contactEmail the contactEmail to set
	 */
	public void setContactEmail(ArrayList<String> contactEmail) {
		this.contactEmail = contactEmail;
	}
	public ContactInfo(String displayName, String _id) {
		DisplayName = displayName;
		this._id = _id;
		phoneNumbers = new ArrayList<String>();
	}
	private String DisplayName;
	private String photoId;
	private String primaryPhoneNumber;
	private String _id;
	private ArrayList<String> phoneNumbers;
	private ArrayList<String> contactEmail;
	public String getPhotoId()
	{
		return photoId;
	}
	public void setPhotoId(String photoId)
	{
		this.photoId = photoId;
	}
	
	public String getDisplayName() {
		return DisplayName;
	}
	public void setDisplayName(String displayName) {
		DisplayName = displayName;
	}
	public String getPhoneNumber() {
		return primaryPhoneNumber;
	}
	public void setPhoneNumber(String photoUri) {
		this.primaryPhoneNumber = photoUri;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public void setContactNumbers(ArrayList<String> numbers)
	{
		phoneNumbers = numbers;
	}
	public ArrayList<String> getPhoneNumbers()
	{
		return phoneNumbers;
	}
	public void setContactNumbers(String numbers)
	{
		//convertimos en array los números
		try {
			String[] values = numbers.split(",");
			for (int i = 0; i < values.length; i++) {
				phoneNumbers.add(values[i]);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	

}
