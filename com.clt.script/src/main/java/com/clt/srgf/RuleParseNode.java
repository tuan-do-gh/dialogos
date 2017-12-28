/*
 * @(#)RuleParseNode.java
 * Created on Thu Sep 16 2004
 *
 * Copyright (c) 2004 CLT Sprachtechnologie GmbH.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of CLT Sprachtechnologie GmbH ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with CLT Sprachtechnologie GmbH.
 */

package com.clt.srgf;

import java.io.PrintWriter;
import java.util.Map;

/**
 * A non-terminal node in the parse tree generated by {@link Grammar#parse}.
 * This represents a rule in the grammar used to generate the parse tree.
 * 
 * @author Daniel Bobbert
 * @version 1.0
 */

public class RuleParseNode
    extends ParseNode {

  private Rule rule;
  private String name;
  private boolean automatic;


  RuleParseNode(Rule rule, ParseOptions options, boolean automatic) {

    this(rule, RuleParseNode.createRuleNodeName(rule, options), automatic);
  }


  private RuleParseNode(Rule rule, String name, boolean automatic) {

    this.rule = rule;
    this.name = name;
    this.automatic = automatic;
  }


  private static String createRuleNodeName(Rule rule, ParseOptions options) {

    String name = rule.getName();
    int l = name.length();
    while ((l > 0) && (name.charAt(l - 1) == options.contextSuffix)) {
      l--;
    }
    name = name.substring(0, l);
    return name;
  }


  /**
   * Return the {@link Rule} that corresponds to this node in the parse tree.
   */
  public Rule getRule() {

    return this.rule;
  }


  public boolean isAutomatic() {

    return this.automatic;
  }


  @Override
  public boolean getAllowsChildren() {

    return true;
  }


  /**
   * Return the number of children of this node.
   */
  public int numChildren() {

    return this.getChildCount();
  }


  /**
   * Return the nth child of this node. Indices range from 0 to
   * {@link #numChildren}-1
   */
  public ParseNode getChild(int index) {

    return this.getChildAt(index);
  }


  ParseNode getLastChild() {

    if (this.numChildren() == 0) {
      return null;
    }
    else {
      return this.getChild(this.numChildren() - 1);
    }
  }


  void addChild(ParseNode child) {

    this.insertChild(child, this.numChildren());
  }


  ParseNode removeLastChild() {

    if (!this.children.isEmpty()) {
      return this.children.remove(this.children.size() - 1);
    }
    else {
      return null;
    }
  }


  @Override
  ParseNode clone(Map<ParseNode, ParseNode> mapping) {

    RuleParseNode n = new RuleParseNode(this.rule, this.name, this.automatic);
    mapping.put(this, n);
    for (ParseNode child : this.children) {
      n.addChild(child.clone(mapping));
    }
    return n;
  }


  boolean removeLastChildIfEmpty() {

    ParseNode lastChild = this.getLastChild();
    if ((lastChild instanceof RuleParseNode)
      && (((RuleParseNode)lastChild).numChildren() == 0)) {
      this.removeLastChild();
      return true;
    }
    else {
      return false;
    }
  }


  @Override
  public int hashCode() {

    return this.rule.hashCode() ^ this.children.hashCode();
  }


  @Override
  public boolean equals(Object o) {

    if (o instanceof RuleParseNode) {
      RuleParseNode n = (RuleParseNode)o;
      return this.rule.equals(n.rule) && this.children.equals(n.children);
    }
    else {
      return false;
    }
  }


  @Override
  public String toString() {

    return this.name;
  }


  @Override
  protected void printHash(PrintWriter w) {

    w.print(this.rule.hashCode() + " ^ " + this.children.hashCode() + " = "
      + this.hashCode());
  }

}