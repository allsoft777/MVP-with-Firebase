/*
 * Copyright (C) 2017 Seongil Kim <kims172@gmail.com>
 *
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
 */

package com.seongil.mvplife.sample.ui.cliplist.adapter.footer;

import com.seongil.recyclerviewlife.model.RecyclerViewFooterItem;
import com.seongil.recyclerviewlife.model.common.ViewStatus;

/**
 * @author seong-il, kim
 * @since 17. 4. 7
 */
public class ClipListFooterItem extends RecyclerViewFooterItem {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================

    // ========================================================================
    // constructors
    // ========================================================================
    public ClipListFooterItem() {
        super(ViewStatus.VISIBLE_LOADING_VIEW);
    }

    public ClipListFooterItem(ViewStatus requestCode) {
        super(requestCode);
    }

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================

    // ========================================================================
    // methods
    // ========================================================================

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
