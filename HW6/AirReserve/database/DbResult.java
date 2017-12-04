/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airreserve.database;

import airreserve.helpers.StringHelpers;

/**
 *
 * @author brand
 */

public class DbResult<T> implements IDbResult<T> {
    
    private T _result;
    private boolean _isError;
    private String _message;
    
    public T getResult() {
        return _result;
    }
    
    public void setResult(T result) {
        _result = result;
    }
    
    public boolean getIsError() {
        return _isError;
    }
    
    public void setIsError(boolean isError) {
        _isError = isError;
    }
    
    public String getMessage() {
        return _message;
    }
    
    public void setMessage(String message) {
        
        if (StringHelpers.isNullOrEmpty(_message)) {
            _message = message;
        }
        else {
            if (!StringHelpers.isNullOrEmpty(message)) {
                _message += "\r\n\r\n" + message;
            }
        }
    }
}
