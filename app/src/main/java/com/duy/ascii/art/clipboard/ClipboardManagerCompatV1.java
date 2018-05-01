/*
 * Copyright (c) 2017 by Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.ascii.art.clipboard;

import android.content.Context;
import android.text.ClipboardManager;

@SuppressWarnings("deprecation")
public class ClipboardManagerCompatV1 implements ClipboardManagerCompat {
    private final ClipboardManager clip;

    public ClipboardManagerCompatV1(Context context) {
        clip = (ClipboardManager) context.getApplicationContext()
                .getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public CharSequence getText() {
        return clip.getText();
    }

    @Override
    public void setText(CharSequence text) {
        clip.setText(text);
    }

    @Override
    public boolean hasText() {
        return clip.hasText();
    }
}
