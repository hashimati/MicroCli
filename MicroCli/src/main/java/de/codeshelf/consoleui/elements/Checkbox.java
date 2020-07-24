package de.codeshelf.consoleui.elements;

import de.codeshelf.consoleui.elements.items.CheckboxItemIF;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Andreas Wegmann
 * Date: 01.01.16
 */
public class Checkbox extends AbstractPromptableElement {

  private List<CheckboxItemIF> checkboxItemList;

  public Checkbox(String message, String name, List<CheckboxItemIF> checkboxItemList) {
    super(message,name);
    this.checkboxItemList = checkboxItemList;
  }

  public String getMessage() {
    return message;
  }

  public ArrayList<CheckboxItemIF> getCheckboxItemList() {
    return new ArrayList<CheckboxItemIF>(checkboxItemList);
  }
}
