package fr.yuki.yrpf.luaapi;

import fr.yuki.yrpf.i18n.I18n;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class AddI18nKeyEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        String lang = objects[0].toString();
        String key = objects[1].toString();
        String value = objects[2].toString();
        I18n.addKeyToLang(lang, key, value);
        return null;
    }
}
