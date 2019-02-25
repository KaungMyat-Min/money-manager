package kaungmyatmin.com.moneymanager.DB;

public interface ValHolder {
int DATABASE_VERSION = 1;
String DATABASE_NAME = "TriO";


String KEY_LAST_CHECK = "lastCheck";
String KEY_USERNAME = "userName";
String KEY_SALARY = "salary";
String KEY_SAVE_AMT= "saveAmt";
String KEY_HOUSE_RENT="houseRent";
String KEY_COST_CLOTH="cost_cloth";
String KEY_COST_TRANSPORT="cost_transport";
String KEY_COST_SMS="cost_sms";
String KEY_COST_TALK="cost_talk";
String KEY_COST_INTERNET="cost_internet";
String KEY_ADDITIONAL_COST_SIZE="additional_data_size";
String TITLE = "title";
String VALUE = "value";
String KEY_IS_DAILY = "isDaily";

String KEY_IS_ALARM_SET = "isAlarmSet";
String KEY_LATEST_CHECK = "latestCheck";


String KEY_MAX_USE_PER_MONTH = "maxUsePerMonth";
String KEY_MAX_USE_PER_DAY = "maxUsePerDay";



//tableNames
 String TABLE_DATA_USAGE ="DataUsage";
 String TABLE_MONEY_USAGE = "MoneyUsage";
 String PHONE_USAGE = "PhoneUsage";
 String SAVE_DEBT = "SaveDebt";

//columns name
// String TITLE = "title";
 String AMT = "Amt";
 String REASON = "reason";
 String MINUTE = "minute";
 String HOUR = "hour";
 String DAY = "Day";
 String MONTH = "Month";
 String YEAR = "Year";
 String MILLISECOND = "millisecond";
 String DATE = "Date";
 String SUM = "totalAmt";
 String ID = "id";
 String USER_NAME = "userName";

 
 

//title types
 String SMS = "SMS";
 String TALK = "TALK";
 
 int USAGE_DAILY = 1;
 int USAGE_MONTLY = 2;
 int TYPE_TOTAL_USAGE = 1;
 int TYPE_TOTAL_SAVE = 2;
 
 int TYPE_INDIVIDUAL_DAILY_USAGE = 5;
 int TYPE_INDIVIDUAL_MONTHLY_USAGE = 6;
}
