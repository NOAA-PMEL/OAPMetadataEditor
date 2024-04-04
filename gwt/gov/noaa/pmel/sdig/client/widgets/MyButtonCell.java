package gov.noaa.pmel.sdig.client.widgets;

/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2013 - 2015 GwtBootstrap3
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Styles;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;
import com.google.gwt.user.client.ui.HasEnabled;

public class MyButtonCell extends com.google.gwt.cell.client.ButtonCell implements HasEnabled {

    private IconType icon;

    private ButtonType type = ButtonType.DEFAULT;

    private ButtonSize size = ButtonSize.DEFAULT;

    private boolean enabled = true;

    public MyButtonCell() {
        super(SimpleSafeHtmlRenderer.getInstance());
    }

    public MyButtonCell(IconType icon, ButtonType type, ButtonSize size) {
        this();
        this.icon = icon;
        this.type = type;
        this.size = size;
    }

    public void setIcon(IconType newIcon) {
        this.icon = newIcon;
    }
    public void setType(ButtonType newType) {
        type = newType;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void render(com.google.gwt.cell.client.Cell.Context context, SafeHtml data, SafeHtmlBuilder sb) {
        StringBuilder bldr = new StringBuilder("btn") //
                .append(" "); //

        String typeCss = type.getCssName();
//        if ( context.getColumn() == 5 && context.getIndex() == 0 ) {
//            typeCss = ButtonType.SUCCESS.getCssName();
//        }
        bldr.append(typeCss) //
            .append(" ") //
            .append(size.getCssName()); //
        String cssClasses = bldr.toString();

        String disabled = "";
        if (!enabled) {
            disabled = " disabled=\"disabled\"";
        }

        sb.appendHtmlConstant("<button type=\"button\" class=\"" + cssClasses + "\" tabindex=\"-1\"" + disabled + ">");
        if (icon != null) {
            String iconCss = icon.getCssName();
//            if ( context.getColumn() == 5 && context.getIndex() == 0 ) {
//                iconCss = IconType.SAVE.getCssName();
//            }
            String iconHtml = new StringBuilder("<i class=\"") //
                    .append(Styles.FONT_AWESOME_BASE) //
                    .append(" ") //
                    .append(iconCss) //
                    .append("\"></i> ") //
                    .toString();
            sb.appendHtmlConstant(iconHtml);
        }
        if (data != null) {
            sb.append(data);
        }
        sb.appendHtmlConstant("</button>");
    }

}
