package com.shachr.common.model.binding.utils;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by shachar on 14/02/2016.
 */

/**
 * Class QueryStringParser is used to _parse Query String and to create value(s) of
 * the given parameter key.
 *
 * @author TapasB
 */
public class QueryStringParser {

    public static final String DEFAUL_CHARSET = "UTF-8";
    private static final Pattern KEY_PATTERN = Pattern.compile("^([^\\[$]+)(?:\\[([^\\]]+)\\])?(?:\\[([^\\]]+)\\])?(?:\\[([^\\]]+)\\])?(?:\\[([^\\]]+)\\])?(?:\\[([^\\]]+)\\])?(?:\\[([^\\]]+)\\])?(?:\\[([^\\]]+)\\])?(?:\\[([^\\]]+)\\])?(?:\\[([^\\]]+)\\])?(?:\\[([^\\]]+)\\])?$",Pattern.CASE_INSENSITIVE);
    private static final Pattern DIGIT_PATTERN = Pattern.compile("^\\d+$",Pattern.CASE_INSENSITIVE);

    private Charset charset;
    private Map<String,Object> _hash;

    /**
     * Constructor QueryStringParser initialises a new instance of this class
     * with the given <tt>queryString</tt>. By default it uses the "UTF-8"
     * Character Encoding.
     *
     * @author TapasB
     * @param queryString
     *            The Query String to _parse
     * @throws NullPointerException
     *             If the given <tt>queryString</tt> is null
     */
    public QueryStringParser(String queryString) throws NullPointerException {
        this(queryString, DEFAUL_CHARSET, null);
    }

    public static Map<String,Object> parse(String queryString, String charsetName, Map<String,Object> initialValue){
        QueryStringParser qs = new QueryStringParser(queryString, charsetName, initialValue);
        return initialValue;
    }

    /**
     * Constructor QueryStringParser
     *
     * @author TapasB
     * @param queryString
     *            The Query String to _parse
     * @param charsetName
     *            The {@link Charset} needs to be used to _parse
     * @throws NullPointerException
     * @throws IllegalCharsetNameException
     *             If the given <tt>charsetName</tt> name is illegal
     * @throws IllegalArgumentException
     *             If the given <tt>charsetName</tt> is null
     * @throws UnsupportedCharsetException
     *             If no support for the named <tt>charsetName</tt> is available
     *             in this instance of the Java virtual machine
     */
    public QueryStringParser(String queryString, String charsetName, Map<String,Object> initialValue) throws NullPointerException, IllegalCharsetNameException, IllegalArgumentException, UnsupportedCharsetException {
        if (initialValue == null) initialValue = new HashMap();
        this._hash = initialValue;

        if (queryString == null) {
            throw new NullPointerException("Query String is Null.");
        }

        this.charset = Charset.forName(charsetName);
        _parse(queryString);
    }

    // This method is used to _parse the query string
    private void _parse(String queryString) {
        for (String pair : queryString.split("&")) {
            int idxOfEqual = pair.indexOf("=");

            if (idxOfEqual < 0) {
                addElement(pair, "");
            } else {
                String key = pair.substring(0, idxOfEqual);
                String value = pair.substring(idxOfEqual + 1);
                addElement(key, value);
            }
        }
    }

    // This method adds the given key and value into the List query.
    // Before adding it decodes the key and value with the given charset.
    public void addElement(String key, String value) {
        if (key == null || value == null) {
            throw new NullPointerException("Key or Value is Null");
        }

        try {
            String charsetName = charset.name();
            key = URLDecoder.decode(key, charsetName);
            value = URLDecoder.decode(value, charsetName);
        } catch (Exception ignore) { return; }

        Matcher matcher = KEY_PATTERN.matcher(key);
        MapBuilder builder = new MapBuilder(_hash);
        while(matcher.find()){
            for(int inx=1;inx<matcher.groupCount();inx++)
                if(matcher.group(inx) != null) {
                    String nestedKey = matcher.group(inx);
                    builder.Key(nestedKey);
                }
                else{
                    break;
                }
        }

        builder.Value(value);
    }

    public class MapBuilder{

        String _key;
        String _lastKey;

        Map<String,Object> _value;

        Map<String,Object> _currentMap;
        List<Object> _currentArray;
        int _lastArrayPosition = -1;

        public MapBuilder(Map<String,Object> initialValue){ _value = initialValue; }

        public void Key(String key){
            if (_key == null) {
                boolean isDigit = DIGIT_PATTERN.matcher(key).find();
                if (isDigit)
                    throw new IllegalArgumentException(key);
                _key = key;
            } else {
                _createLastKey(key);
            }
            _lastKey = key;

        }

        private void _createLastKey(String key){
            boolean isDigit = DIGIT_PATTERN.matcher(key).find();
            // we got another key e.g. user[info][name]
            if( !isDigit ) {

                // Map
                if (_currentMap != null){
                    if (!_currentMap.containsKey(_lastKey)) {
                        Map<String,Object> value = new HashMap<String, Object>();
                        _currentMap.put(_lastKey, value);
                        _currentMap = value;
                    } else {
                        _currentMap = (HashMap<String,Object>)_currentMap.get(_lastKey);
                    }
                    // Array
                } else if (_currentArray != null){
                    if(_lastArrayPosition == -1)
                        throw new IllegalArgumentException(key);
                    Map<String,Object> value = new HashMap<String, Object>();
                    if (_currentArray.size() > _lastArrayPosition){
                        _currentMap = (HashMap<String,Object>)_currentArray.get(_lastArrayPosition);
                    }
                    else {
                        _currentArray.add(_lastArrayPosition, value);
                        _currentMap = value;
                    }
                }
                // Initial value
                else {
                    if (!_value.containsKey(_lastKey)) {
                        Map<String,Object> value = new HashMap<String, Object>();
                        _currentMap = value;
                        _value.put(_lastKey, value);
                    } else {
                        _currentMap = (HashMap<String,Object>)_value.get(_lastKey);
                    }
                }

                // reset last array position
                _lastArrayPosition = -1;

            }

            // we got a digit user[info][hobbies][0],
            else{
                if (_currentMap != null){
                    if (!_currentMap.containsKey(_lastKey)) {
                        ArrayList<Object> value = new ArrayList<Object>();
                        _currentMap.put(_lastKey, value);
                        _currentArray = value;
                    } else {
                        _currentArray = (ArrayList<Object>)_currentMap.get(_lastKey);
                    }
                } else if ( _value != null ) {
                    if (!_value.containsKey(_lastKey)) {
                        ArrayList<Object> value = new ArrayList<Object>();
                        _value.put(_lastKey, value);
                        _currentArray = value;
                    } else {
                        _currentArray = (ArrayList<Object>)_value.get(_lastKey);
                    }
                } else {
                    throw new IllegalArgumentException(key);
                }

                _lastArrayPosition = Integer.parseInt(key);
                _currentMap = null;
            }
        }

        public void Value(Object value){
            if (_currentArray != null){

                if(_currentArray.size() <= _lastArrayPosition) {
                    for (int inx = _currentArray.size(); inx <= _lastArrayPosition; inx++) {
                        if (inx == _lastArrayPosition)
                            _currentArray.add(value);
                        else
                            _currentArray.add(null);
                    }
                }
                else {
                    _currentArray.add(_lastArrayPosition, value);
                }
            }
            else if (_currentMap != null){
                _currentMap.put(_lastKey, value);
            }
            else {
                _value.put(_lastKey, value);
            }
        }

    }
}

