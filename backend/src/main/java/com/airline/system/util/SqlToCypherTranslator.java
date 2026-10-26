package com.airline.system.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlToCypherTranslator {

    private static final Map<String, String> TABLE_TO_LABEL = new HashMap<>();
    private static final Map<String, String> COLUMN_MAP = new HashMap<>();
    private static final Set<String> ID_PROPERTIES = new HashSet<>();
    private static final Set<String> NUMERIC_PROPERTIES = new HashSet<>();
    private static final Set<String> DATE_PROPERTIES = new HashSet<>();
    private static final Set<String> TIME_PROPERTIES = new HashSet<>();
    private static final Map<String, List<String>> TABLE_DEFAULT_COLUMNS = new HashMap<>();

    static {
        TABLE_TO_LABEL.put("AIRLINE", "Airline");
        TABLE_TO_LABEL.put("AIRLINES", "Airline");
        TABLE_TO_LABEL.put("AIRPORT", "Airport");
        TABLE_TO_LABEL.put("AIRPORTS", "Airport");
        TABLE_TO_LABEL.put("BAGGAGE", "Baggage");
        TABLE_TO_LABEL.put("BAGGAGES", "Baggage");
        TABLE_TO_LABEL.put("BAGGAGE_TRACKING", "BaggageTracking");
        TABLE_TO_LABEL.put("BAGGAGETRACKING", "BaggageTracking");
        TABLE_TO_LABEL.put("BAGGAGE_TRACKINGS", "BaggageTracking");
        TABLE_TO_LABEL.put("EMPLOYEE", "Employee");
        TABLE_TO_LABEL.put("EMPLOYEES", "Employee");
        TABLE_TO_LABEL.put("FLIGHT", "Flight");
        TABLE_TO_LABEL.put("FLIGHTS", "Flight");
        TABLE_TO_LABEL.put("PASSENGER", "Passenger");
        TABLE_TO_LABEL.put("PASSENGERS", "Passenger");
        TABLE_TO_LABEL.put("PASSENGER_PHONE", "PassengerPhone");
        TABLE_TO_LABEL.put("PASSENGERPHONE", "PassengerPhone");
        TABLE_TO_LABEL.put("PASSENGER_PHONES", "PassengerPhone");
        TABLE_TO_LABEL.put("PAYMENT", "Payment");
        TABLE_TO_LABEL.put("PAYMENTS", "Payment");
        TABLE_TO_LABEL.put("RESERVATION", "Reservation");
        TABLE_TO_LABEL.put("RESERVATIONS", "Reservation");
        TABLE_TO_LABEL.put("TICKET", "Ticket");
        TABLE_TO_LABEL.put("TICKETS", "Ticket");
        TABLE_TO_LABEL.put("TICKET_BAGGAGE", "TicketBaggage");
        TABLE_TO_LABEL.put("TICKETBAGGAGE", "TicketBaggage");

        COLUMN_MAP.put("AIRLINEID", "airlineID");
        COLUMN_MAP.put("AIRLINE_ID", "airlineID");
        COLUMN_MAP.put("AIRLINENAME", "airlineName");
        COLUMN_MAP.put("AIRLINE_NAME", "airlineName");
        COLUMN_MAP.put("IATACODE", "iataCode");
        COLUMN_MAP.put("IATA_CODE", "iataCode");
        COLUMN_MAP.put("IATA", "iataCode");

        COLUMN_MAP.put("AIRPORTID", "airportID");
        COLUMN_MAP.put("AIRPORT_ID", "airportID");
        COLUMN_MAP.put("AIRPORTCODE", "airportID");
        COLUMN_MAP.put("AIRPORT_CODE", "airportID");
        COLUMN_MAP.put("AIRPORTNAME", "airportName");
        COLUMN_MAP.put("AIRPORT_NAME", "airportName");
        COLUMN_MAP.put("CITY", "city");
        COLUMN_MAP.put("COUNTRY", "country");

        COLUMN_MAP.put("BAGGAGEID", "baggageID");
        COLUMN_MAP.put("BAGGAGE_ID", "baggageID");
        COLUMN_MAP.put("NOOFPIECES", "noOfPieces");
        COLUMN_MAP.put("NO_OF_PIECES", "noOfPieces");
        COLUMN_MAP.put("PIECES", "noOfPieces");
        COLUMN_MAP.put("WEIGHT", "weight");

        COLUMN_MAP.put("TRACKINGID", "trackingID");
        COLUMN_MAP.put("TRACKING_ID", "trackingID");
        COLUMN_MAP.put("SCANLOCATION", "scanLocation");
        COLUMN_MAP.put("SCAN_LOCATION", "scanLocation");
        COLUMN_MAP.put("SCANTIME", "scanTime");
        COLUMN_MAP.put("SCAN_TIME", "scanTime");

        COLUMN_MAP.put("EMPLOYEEID", "employeeID");
        COLUMN_MAP.put("EMPLOYEE_ID", "employeeID");
        COLUMN_MAP.put("DESIGNATION", "designation");
        COLUMN_MAP.put("JOBTITLE", "designation");
        COLUMN_MAP.put("JOB_TITLE", "designation");
        COLUMN_MAP.put("PHONE", "phone");
        COLUMN_MAP.put("PHONENUMBER", "phone");
        COLUMN_MAP.put("PHONE_NUMBER", "phone");

        COLUMN_MAP.put("FLIGHTID", "flightID");
        COLUMN_MAP.put("FLIGHT_ID", "flightID");
        COLUMN_MAP.put("FLIGHTNUMBER", "flightID");
        COLUMN_MAP.put("FLIGHT_NUMBER", "flightID");
        COLUMN_MAP.put("DEPARTURETIME", "departureTime");
        COLUMN_MAP.put("DEPARTURE_TIME", "departureTime");

        COLUMN_MAP.put("PASSENGERID", "passengerID");
        COLUMN_MAP.put("PASSENGER_ID", "passengerID");
        COLUMN_MAP.put("NAME", "name");
        COLUMN_MAP.put("PASSENGERNAME", "name");
        COLUMN_MAP.put("PASSENGER_NAME", "name");
        COLUMN_MAP.put("EMAIL", "email");
        COLUMN_MAP.put("DOB", "dob");
        COLUMN_MAP.put("DATEOFBIRTH", "dob");
        COLUMN_MAP.put("DATE_OF_BIRTH", "dob");
        COLUMN_MAP.put("STREET", "street");
        COLUMN_MAP.put("PIN", "pin");
        COLUMN_MAP.put("PINCODE", "pin");
        COLUMN_MAP.put("PIN_CODE", "pin");
        COLUMN_MAP.put("POSTALCODE", "pin");
        COLUMN_MAP.put("ZIPCODE", "pin");

        COLUMN_MAP.put("PAYMENTID", "paymentID");
        COLUMN_MAP.put("PAYMENT_ID", "paymentID");
        COLUMN_MAP.put("PAYMENTMETHOD", "paymentMethod");
        COLUMN_MAP.put("PAYMENT_METHOD", "paymentMethod");
        COLUMN_MAP.put("AMOUNT", "amount");

        COLUMN_MAP.put("RESERVATIONID", "reservationID");
        COLUMN_MAP.put("RESERVATION_ID", "reservationID");
        COLUMN_MAP.put("BOOKINGSTATUS", "bookingStatus");
        COLUMN_MAP.put("BOOKING_STATUS", "bookingStatus");
        COLUMN_MAP.put("STATUS", "bookingStatus");
        COLUMN_MAP.put("RESERVATIONCLASS", "reservationClass");
        COLUMN_MAP.put("RESERVATION_CLASS", "reservationClass");
        COLUMN_MAP.put("CLASS", "reservationClass");
        COLUMN_MAP.put("SEATCLASS", "reservationClass");
        COLUMN_MAP.put("DEPARTUREAIRPORTID", "departureAirportID");
        COLUMN_MAP.put("DEPARTURE_AIRPORT_ID", "departureAirportID");
        COLUMN_MAP.put("DEPARTUREAIRPORT", "departureAirportID");
        COLUMN_MAP.put("ARRIVALAIRPORTID", "arrivalAirportID");
        COLUMN_MAP.put("ARRIVAL_AIRPORT_ID", "arrivalAirportID");
        COLUMN_MAP.put("ARRIVALAIRPORT", "arrivalAirportID");

        COLUMN_MAP.put("TICKETNO", "ticketNo");
        COLUMN_MAP.put("TICKET_NO", "ticketNo");
        COLUMN_MAP.put("TICKETID", "ticketNo");
        COLUMN_MAP.put("TICKET_ID", "ticketNo");
        COLUMN_MAP.put("SEATNO", "seatNo");
        COLUMN_MAP.put("SEAT_NO", "seatNo");
        COLUMN_MAP.put("FARE", "fare");
        COLUMN_MAP.put("PRICE", "fare");

        COLUMN_MAP.put("CHECKEDINDATE", "checkedInDate");
        COLUMN_MAP.put("CHECKED_IN_DATE", "checkedInDate");
        COLUMN_MAP.put("ID", "id");

        ID_PROPERTIES.add("passengerID");
        ID_PROPERTIES.add("airlineID");
        ID_PROPERTIES.add("airportID");
        ID_PROPERTIES.add("baggageID");
        ID_PROPERTIES.add("flightID");
        ID_PROPERTIES.add("trackingID");
        ID_PROPERTIES.add("employeeID");
        ID_PROPERTIES.add("paymentID");
        ID_PROPERTIES.add("reservationID");
        ID_PROPERTIES.add("ticketNo");
        ID_PROPERTIES.add("id");
        ID_PROPERTIES.add("departureAirportID");
        ID_PROPERTIES.add("arrivalAirportID");
        ID_PROPERTIES.add("phone");
        ID_PROPERTIES.add("pin");

        NUMERIC_PROPERTIES.add("noOfPieces");
        NUMERIC_PROPERTIES.add("weight");
        NUMERIC_PROPERTIES.add("fare");
        NUMERIC_PROPERTIES.add("amount");

        DATE_PROPERTIES.add("dob");
        DATE_PROPERTIES.add("checkedInDate");

        TIME_PROPERTIES.add("departureTime");
        TIME_PROPERTIES.add("scanTime");

        TABLE_DEFAULT_COLUMNS.put("Passenger", Arrays.asList("passengerID", "name", "email", "dob", "street", "city", "pin"));
        TABLE_DEFAULT_COLUMNS.put("Flight", Arrays.asList("flightID", "airlineID", "departureTime"));
        TABLE_DEFAULT_COLUMNS.put("Airport", Arrays.asList("airportID", "airportName", "city", "country"));
        TABLE_DEFAULT_COLUMNS.put("Airline", Arrays.asList("airlineID", "airlineName", "iataCode"));
        TABLE_DEFAULT_COLUMNS.put("Reservation", Arrays.asList("reservationID", "passengerID", "bookingStatus", "reservationClass", "departureAirportID", "arrivalAirportID"));
        TABLE_DEFAULT_COLUMNS.put("Ticket", Arrays.asList("ticketNo", "reservationID", "seatNo", "fare"));
        TABLE_DEFAULT_COLUMNS.put("Payment", Arrays.asList("paymentID", "reservationID", "ticketNo", "amount", "paymentMethod"));
        TABLE_DEFAULT_COLUMNS.put("Baggage", Arrays.asList("baggageID", "noOfPieces", "weight"));
        TABLE_DEFAULT_COLUMNS.put("BaggageTracking", Arrays.asList("trackingID", "baggageID", "scanLocation", "scanTime"));
        TABLE_DEFAULT_COLUMNS.put("Employee", Arrays.asList("employeeID", "name", "designation", "phone"));
        TABLE_DEFAULT_COLUMNS.put("PassengerPhone", Arrays.asList("phone", "passengerID"));
        TABLE_DEFAULT_COLUMNS.put("TicketBaggage", Arrays.asList("reservationID", "ticketNo", "baggageID", "checkedInDate"));
    }

    public static String translate(String sqlQuery) {
        if (sqlQuery == null) {
            return "";
        }
        String sql = sqlQuery.trim();
        while (sql.endsWith(";")) {
            sql = sql.substring(0, sql.length() - 1).trim();
        }

        String upper = sql.toUpperCase();
        if (upper.startsWith("MATCH") || upper.startsWith("CREATE") || upper.startsWith("MERGE")
                || upper.startsWith("CALL") || upper.startsWith("SHOW") || upper.startsWith("RETURN")) {
            return ensureDetachDelete(sql);
        }

        if (upper.startsWith("SELECT")) {
            return translateSelect(sql);
        } else if (upper.startsWith("DELETE")) {
            return translateDelete(sql);
        } else if (upper.startsWith("INSERT")) {
            return translateInsert(sql);
        } else if (upper.startsWith("UPDATE")) {
            return translateUpdate(sql);
        }

        return sql;
    }

    private static String ensureDetachDelete(String cypher) {
        return cypher.replaceAll("(?i)(?<!DETACH\\s)\\bDELETE\\b", "DETACH DELETE");
    }

    private static String translateSelect(String sql) {
        Pattern pattern = Pattern.compile("(?i)^SELECT\\s+(.+?)\\s+FROM\\s+([A-Za-z0-9_]+)(?:\\s+AS\\s+[A-Za-z0-9_]+|\\s+[A-Za-z0-9_]+)?(?:\\s+WHERE\\s+(.+?))?(?:\\s+ORDER\\s+BY\\s+(.+?))?(?:\\s+LIMIT\\s+(\\d+))?$");
        Matcher matcher = pattern.matcher(sql);

        if (!matcher.find()) {
            return "MATCH (n) RETURN n LIMIT 25";
        }

        String columns = matcher.group(1).trim();
        String table = matcher.group(2).trim();
        String whereClause = matcher.group(3);
        String orderByClause = matcher.group(4);
        String limitClause = matcher.group(5);

        String label = resolveLabel(table);
        StringBuilder cypher = new StringBuilder();
        cypher.append("MATCH (n:").append(label).append(")");

        if (whereClause != null && !whereClause.trim().isEmpty()) {
            cypher.append(" WHERE ").append(translateConditions(whereClause.trim()));
        }

        if (columns.equals("*")) {
            cypher.append(" RETURN n");
        } else if (columns.equalsIgnoreCase("COUNT(*)") || columns.equalsIgnoreCase("COUNT(1)")) {
            cypher.append(" RETURN count(n) AS count");
        } else {
            List<String> colTokens = splitSqlList(columns);
            StringBuilder returnCols = new StringBuilder();
            for (int i = 0; i < colTokens.size(); i++) {
                String token = colTokens.get(i).trim();
                String alias = null;
                String colName = token;

                Pattern asPattern = Pattern.compile("(?i)^(.+?)\\s+AS\\s+(.+?)$");
                Matcher asMatcher = asPattern.matcher(token);
                if (asMatcher.find()) {
                    colName = asMatcher.group(1).trim();
                    alias = asMatcher.group(2).trim();
                }

                String prop = resolveProperty(colName);
                if (i > 0) {
                    returnCols.append(", ");
                }
                returnCols.append("n.").append(prop);
                if (alias != null) {
                    returnCols.append(" AS ").append(alias);
                } else if (!prop.equalsIgnoreCase(colName)) {
                    returnCols.append(" AS ").append(colName);
                }
            }
            cypher.append(" RETURN ").append(returnCols);
        }

        if (orderByClause != null && !orderByClause.trim().isEmpty()) {
            cypher.append(" ORDER BY ").append(translateOrderBy(orderByClause.trim()));
        }

        if (limitClause != null && !limitClause.trim().isEmpty()) {
            cypher.append(" LIMIT ").append(limitClause.trim());
        }

        return cypher.toString();
    }

    private static String translateDelete(String sql) {
        Pattern pattern = Pattern.compile("(?i)^DELETE\\s+(?:FROM\\s+)?([A-Za-z0-9_]+)(?:\\s+AS\\s+[A-Za-z0-9_]+|\\s+[A-Za-z0-9_]+)?(?:\\s+WHERE\\s+(.+?))?$");
        Matcher matcher = pattern.matcher(sql);

        if (!matcher.find()) {
            return sql;
        }

        String table = matcher.group(1).trim();
        String whereClause = matcher.group(2);

        String label = resolveLabel(table);
        StringBuilder cypher = new StringBuilder();
        cypher.append("MATCH (n:").append(label).append(")");

        if (whereClause != null && !whereClause.trim().isEmpty()) {
            cypher.append(" WHERE ").append(translateConditions(whereClause.trim()));
        }

        cypher.append(" DETACH DELETE n");
        return cypher.toString();
    }

    private static String translateInsert(String sql) {
        Pattern withColsPattern = Pattern.compile("(?i)^INSERT\\s+INTO\\s+([A-Za-z0-9_]+)\\s*\\((.+?)\\)\\s*VALUES\\s*\\((.+?)\\)$");
        Matcher m1 = withColsPattern.matcher(sql);

        if (m1.find()) {
            String table = m1.group(1).trim();
            List<String> cols = splitSqlList(m1.group(2));
            List<String> vals = splitSqlList(m1.group(3));

            String label = resolveLabel(table);
            StringBuilder props = new StringBuilder();

            int count = Math.min(cols.size(), vals.size());
            String reservationIdVal = null;
            String ticketNoVal = null;
            String baggageIdVal = null;
            boolean hasIdProp = false;

            for (int i = 0; i < count; i++) {
                String prop = resolveProperty(cols.get(i).trim());
                String formattedVal = formatPropertyValue(prop, vals.get(i).trim());

                if (prop.equals("reservationID")) reservationIdVal = unquote(formattedVal);
                if (prop.equals("ticketNo")) ticketNoVal = unquote(formattedVal);
                if (prop.equals("baggageID")) baggageIdVal = unquote(formattedVal);
                if (prop.equals("id")) hasIdProp = true;

                if (i > 0) {
                    props.append(", ");
                }
                props.append(prop).append(": ").append(formattedVal);
            }

            if ("TicketBaggage".equals(label) && !hasIdProp && reservationIdVal != null && ticketNoVal != null && baggageIdVal != null) {
                String compositeId = reservationIdVal + "_" + ticketNoVal + "_" + baggageIdVal;
                props.append(", id: '").append(compositeId).append("'");
            }

            return "CREATE (n:" + label + " {" + props + "}) RETURN n";
        }

        Pattern noColsPattern = Pattern.compile("(?i)^INSERT\\s+INTO\\s+([A-Za-z0-9_]+)\\s+VALUES\\s*\\((.+?)\\)$");
        Matcher m2 = noColsPattern.matcher(sql);

        if (m2.find()) {
            String table = m2.group(1).trim();
            List<String> vals = splitSqlList(m2.group(2));
            String label = resolveLabel(table);

            List<String> defaultCols = TABLE_DEFAULT_COLUMNS.get(label);
            if (defaultCols != null) {
                StringBuilder props = new StringBuilder();
                int count = Math.min(defaultCols.size(), vals.size());
                for (int i = 0; i < count; i++) {
                    String prop = defaultCols.get(i);
                    String formattedVal = formatPropertyValue(prop, vals.get(i).trim());
                    if (i > 0) {
                        props.append(", ");
                    }
                    props.append(prop).append(": ").append(formattedVal);
                }
                return "CREATE (n:" + label + " {" + props + "}) RETURN n";
            }
        }

        return sql;
    }

    private static String translateUpdate(String sql) {
        Pattern pattern = Pattern.compile("(?i)^UPDATE\\s+([A-Za-z0-9_]+)(?:\\s+AS\\s+[A-Za-z0-9_]+|\\s+[A-Za-z0-9_]+)?\\s+SET\\s+(.+?)(?:\\s+WHERE\\s+(.+?))?$");
        Matcher matcher = pattern.matcher(sql);

        if (!matcher.find()) {
            return sql;
        }

        String table = matcher.group(1).trim();
        String setPart = matcher.group(2).trim();
        String wherePart = matcher.group(3);

        String label = resolveLabel(table);
        StringBuilder cypher = new StringBuilder();
        cypher.append("MATCH (n:").append(label).append(")");

        if (wherePart != null && !wherePart.trim().isEmpty()) {
            cypher.append(" WHERE ").append(translateConditions(wherePart.trim()));
        }

        cypher.append(" SET ");
        List<String> assignments = splitSqlList(setPart);
        for (int i = 0; i < assignments.size(); i++) {
            String assignment = assignments.get(i);
            String[] kv = assignment.split("=", 2);
            if (kv.length == 2) {
                if (i > 0) {
                    cypher.append(", ");
                }
                String prop = resolveProperty(kv[0].trim());
                String formattedVal = formatPropertyValue(prop, kv[1].trim());
                cypher.append("n.").append(prop).append(" = ").append(formattedVal);
            }
        }

        cypher.append(" RETURN n");
        return cypher.toString();
    }

    public static String translateConditions(String condition) {
        String result = condition;

        Pattern isNullPattern = Pattern.compile("(?i)([A-Za-z0-9_.]+)\\s+IS\\s+NULL");
        Matcher mNull = isNullPattern.matcher(result);
        StringBuffer sbNull = new StringBuffer();
        while (mNull.find()) {
            String prop = resolveProperty(mNull.group(1));
            mNull.appendReplacement(sbNull, "n." + prop + " IS NULL");
        }
        mNull.appendTail(sbNull);
        result = sbNull.toString();

        Pattern isNotNullPattern = Pattern.compile("(?i)([A-Za-z0-9_.]+)\\s+IS\\s+NOT\\s+NULL");
        Matcher mNotNull = isNotNullPattern.matcher(result);
        StringBuffer sbNotNull = new StringBuffer();
        while (mNotNull.find()) {
            String prop = resolveProperty(mNotNull.group(1));
            mNotNull.appendReplacement(sbNotNull, "n." + prop + " IS NOT NULL");
        }
        mNotNull.appendTail(sbNotNull);
        result = sbNotNull.toString();

        Pattern likeAny = Pattern.compile("(?i)([A-Za-z0-9_.]+)\\s+LIKE\\s+'%([^%']+)%'");
        Matcher m1 = likeAny.matcher(result);
        StringBuffer sb1 = new StringBuffer();
        while (m1.find()) {
            String prop = resolveProperty(m1.group(1));
            m1.appendReplacement(sb1, "n." + prop + " CONTAINS '" + m1.group(2) + "'");
        }
        m1.appendTail(sb1);
        result = sb1.toString();

        Pattern likeStart = Pattern.compile("(?i)([A-Za-z0-9_.]+)\\s+LIKE\\s+'([^%']+)%'");
        Matcher m2 = likeStart.matcher(result);
        StringBuffer sb2 = new StringBuffer();
        while (m2.find()) {
            String prop = resolveProperty(m2.group(1));
            m2.appendReplacement(sb2, "n." + prop + " STARTS WITH '" + m2.group(2) + "'");
        }
        m2.appendTail(sb2);
        result = sb2.toString();

        Pattern likeEnd = Pattern.compile("(?i)([A-Za-z0-9_.]+)\\s+LIKE\\s+'%([^%']+)'");
        Matcher mEnd = likeEnd.matcher(result);
        StringBuffer sbEnd = new StringBuffer();
        while (mEnd.find()) {
            String prop = resolveProperty(mEnd.group(1));
            mEnd.appendReplacement(sbEnd, "n." + prop + " ENDS WITH '" + mEnd.group(2) + "'");
        }
        mEnd.appendTail(sbEnd);
        result = sbEnd.toString();

        Pattern inPattern = Pattern.compile("(?i)([A-Za-z0-9_.]+)\\s+IN\\s*\\(([^)]+)\\)");
        Matcher m3 = inPattern.matcher(result);
        StringBuffer sb3 = new StringBuffer();
        while (m3.find()) {
            String prop = resolveProperty(m3.group(1));
            List<String> rawItems = splitSqlList(m3.group(2));
            StringBuilder formattedItems = new StringBuilder();
            for (int i = 0; i < rawItems.size(); i++) {
                if (i > 0) {
                    formattedItems.append(", ");
                }
                formattedItems.append(formatPropertyValue(prop, rawItems.get(i)));
            }
            m3.appendReplacement(sb3, "n." + prop + " IN [" + formattedItems + "]");
        }
        m3.appendTail(sb3);
        result = sb3.toString();

        Pattern compPattern = Pattern.compile("([A-Za-z0-9_.]+)\\s*(=|!=|<>|>=|<=|>|<)\\s*('(?:[^']|'')*'|\"(?:[^\"]|\"\")*\"|[^\\s()AND|OR]+)");
        Matcher m4 = compPattern.matcher(result);
        StringBuffer sb4 = new StringBuffer();
        while (m4.find()) {
            String col = m4.group(1);
            String op = m4.group(2);
            String val = m4.group(3);

            if (op.equals("<>")) {
                op = "!=";
            }

            String prop = resolveProperty(col);
            String formattedVal = formatPropertyValue(prop, val);

            m4.appendReplacement(sb4, "n." + prop + " " + op + " " + Matcher.quoteReplacement(formattedVal));
        }
        m4.appendTail(sb4);
        return sb4.toString();
    }

    private static String translateOrderBy(String orderBy) {
        List<String> parts = splitSqlList(orderBy);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.size(); i++) {
            String token = parts.get(i).trim();
            String[] words = token.split("\\s+");
            String col = words[0];
            String dir = words.length > 1 ? (" " + words[1].toUpperCase()) : "";
            if (i > 0) {
                sb.append(", ");
            }
            sb.append("n.").append(resolveProperty(col)).append(dir);
        }
        return sb.toString();
    }

    public static String resolveLabel(String tableName) {
        if (tableName == null || tableName.trim().isEmpty()) {
            return "Unknown";
        }
        String clean = tableName.trim().replaceAll("[\"`\\[\\]]", "");
        String upper = clean.toUpperCase().replace("-", "_");
        if (TABLE_TO_LABEL.containsKey(upper)) {
            return TABLE_TO_LABEL.get(upper);
        }
        String[] parts = upper.split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                sb.append(Character.toUpperCase(part.charAt(0)));
                if (part.length() > 1) {
                    sb.append(part.substring(1).toLowerCase());
                }
            }
        }
        return sb.length() > 0 ? sb.toString() : clean;
    }

    public static String resolveProperty(String colName) {
        if (colName == null || colName.trim().isEmpty()) {
            return "prop";
        }
        String clean = colName.trim().replaceAll("[\"`\\[\\]]", "");
        if (clean.contains(".")) {
            clean = clean.substring(clean.lastIndexOf('.') + 1);
        }
        String upper = clean.toUpperCase().replace("-", "_");
        if (COLUMN_MAP.containsKey(upper)) {
            return COLUMN_MAP.get(upper);
        }
        if (clean.contains("_")) {
            String[] parts = clean.toLowerCase().split("_");
            StringBuilder sb = new StringBuilder(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                if (!parts[i].isEmpty()) {
                    sb.append(Character.toUpperCase(parts[i].charAt(0)));
                    if (parts[i].length() > 1) {
                        sb.append(parts[i].substring(1));
                    }
                }
            }
            return sb.toString();
        }
        if (clean.matches("^[A-Z0-9]+$")) {
            return clean.toLowerCase();
        }
        return Character.toLowerCase(clean.charAt(0)) + clean.substring(1);
    }

    public static String formatPropertyValue(String prop, String rawValue) {
        if (rawValue == null) {
            return "null";
        }
        String val = rawValue.trim();
        if (val.equalsIgnoreCase("NULL")) {
            return "null";
        }

        if (isIdProperty(prop)) {
            String unquoted = unquote(val);
            return "'" + unquoted.replace("'", "\\'") + "'";
        }

        if (isNumericProperty(prop)) {
            String unquoted = unquote(val);
            if (unquoted.matches("^-?\\d+(\\.\\d+)?$")) {
                return unquoted;
            }
            return val;
        }

        if (isDateProperty(prop)) {
            String unquoted = unquote(val);
            if (unquoted.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                return "date('" + unquoted + "')";
            }
            return "'" + unquoted.replace("'", "\\'") + "'";
        }

        if (isTimeProperty(prop)) {
            String unquoted = unquote(val);
            if (unquoted.matches("^\\d{2}:\\d{2}(:\\d{2})?$")) {
                if (unquoted.length() == 5) {
                    unquoted = unquoted + ":00";
                }
                return "localtime('" + unquoted + "')";
            }
            return "'" + unquoted.replace("'", "\\'") + "'";
        }

        if (val.equalsIgnoreCase("true") || val.equalsIgnoreCase("false")) {
            return val.toLowerCase();
        }

        if ((val.startsWith("'") && val.endsWith("'")) || (val.startsWith("\"") && val.endsWith("\""))) {
            String inner = unquote(val);
            return "'" + inner.replace("'", "\\'") + "'";
        }

        if (val.matches("^-?\\d+(\\.\\d+)?$")) {
            return val;
        }

        return "'" + val.replace("'", "\\'") + "'";
    }

    public static boolean isIdProperty(String propName) {
        if (propName == null) {
            return false;
        }
        if (ID_PROPERTIES.contains(propName)) {
            return true;
        }
        String lower = propName.toLowerCase();
        return lower.endsWith("id") || lower.equals("id") || lower.endsWith("no") || lower.equals("phone") || lower.equals("pin");
    }

    public static boolean isNumericProperty(String propName) {
        if (propName == null) {
            return false;
        }
        return NUMERIC_PROPERTIES.contains(propName);
    }

    public static boolean isDateProperty(String propName) {
        if (propName == null) {
            return false;
        }
        return DATE_PROPERTIES.contains(propName) || propName.toLowerCase().contains("date") || propName.equalsIgnoreCase("dob");
    }

    public static boolean isTimeProperty(String propName) {
        if (propName == null) {
            return false;
        }
        return TIME_PROPERTIES.contains(propName) || propName.toLowerCase().contains("time");
    }

    private static String unquote(String val) {
        if (val == null) {
            return "";
        }
        String s = val.trim();
        if ((s.startsWith("'") && s.endsWith("'")) || (s.startsWith("\"") && s.endsWith("\""))) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }

    private static List<String> splitSqlList(String input) {
        List<String> list = new ArrayList<>();
        if (input == null) {
            return list;
        }
        StringBuilder current = new StringBuilder();
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '\'' && !inDoubleQuote) {
                inSingleQuote = !inSingleQuote;
                current.append(c);
            } else if (c == '"' && !inSingleQuote) {
                inDoubleQuote = !inDoubleQuote;
                current.append(c);
            } else if (c == ',' && !inSingleQuote && !inDoubleQuote) {
                list.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        if (current.length() > 0) {
            list.add(current.toString().trim());
        }
        return list;
    }
}
