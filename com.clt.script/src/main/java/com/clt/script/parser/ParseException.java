/*
 * @(#)ParseException.java
 * Created on 25.01.2007 by dabo
 *
 * Copyright (c) CLT Sprachtechnologie GmbH.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of CLT Sprachtechnologie GmbH ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with CLT Sprachtechnologie GmbH.
 */

package com.clt.script.parser;

/**
 * @author dabo
 * 
 */
public class ParseException extends Exception {

  public ParseException(String s) {

    super(s);
  }


  @Override
  public String toString() {

    return this.getLocalizedMessage();
  }
}