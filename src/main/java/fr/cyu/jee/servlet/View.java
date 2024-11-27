package fr.cyu.jee.servlet;

public sealed interface View {

    record Forward(String path) implements View {}

    record Redirect(String path) implements View {}

    static View parse(String jspPrefix, String text) {
        return text.startsWith("redirect:") ? new Redirect(text.substring(9)) : new Forward(jspPrefix + "/" + text + ".jsp");
    }
}
