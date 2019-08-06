/*
 * Copyright (C) 2008 ZXing authors
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

package com.sjxd.invoicecheck.util.zxing;

import android.content.Intent;

/**
 * This class provides the constants to use when sending an Intent to Barcode Scanner.
 * These strings are effectively API and cannot be changed.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class Intents {
    private Intents() {
    }

    /**
     * Constants related to the {@link Scan#ACTION} Intent.
     */
    public static final class Scan {
        /**
         * Send this intent to open the Barcodes app in scanning mode, find a barcode, and return
         * the results.
         */
        public static final String ACTION = "com.sjxd.invoicecheck.util.zxing.SCAN";

        /**
         * By default, sending this will decode all barcodes that we understand. However it
         * may be useful to limit scanning to certain formats. Use
         * {@link Intent#putExtra(String, String)} with one of the values below.
         * <p>
         * Setting this is effectively shorthand for setting explicit formats with {@link #FORMATS}.
         * It is overridden by that setting.
         */
        public static final String MODE = "SCAN_MODE";

        /**
         * Decode only UPC and EAN barcodes. This is the right choice for shopping apps which get
         * prices, reviews, etc. for products.
         */
        public static final String PRODUCT_MODE = "PRODUCT_MODE";

        /**
         * Decode only 1D barcodes.
         */
        public static final String ONE_D_MODE = "ONE_D_MODE";

        /**
         * Decode only QR codes.
         */
        public static final String QR_CODE_MODE = "QR_CODE_MODE";

        /**
         * Decode only Data Matrix codes.
         */
        public static final String DATA_MATRIX_MODE = "DATA_MATRIX_MODE";

        /**
         * Decode only Aztec.
         */
        public static final String AZTEC_MODE = "AZTEC_MODE";

        /**
         * Decode only PDF417.
         */
        public static final String PDF417_MODE = "PDF417_MODE";

        /**
         * Comma-separated list of formats to scan for. The values must match the names of
         * {@link com.google.zxing.BarcodeFormat}s, e.g. {@link com.google.zxing.BarcodeFormat#EAN_13}.
         * Example: "EAN_13,EAN_8,QR_CODE". This overrides {@link #MODE}.
         */
        public static final String FORMATS = "SCAN_FORMATS";

        /**
         * Optional parameter to specify the id of the camera from which to recognize barcodes.
         * Overrides the default camera that would otherwise would have been selected.
         * If provided, should be an int.
         */
        public static final String CAMERA_ID = "SCAN_CAMERA_ID";

        /**
         * @see com.google.zxing.DecodeHintType#CHARACTER_SET
         */
        public static final String CHARACTER_SET = "CHARACTER_SET";

        /**
         * Optional parameters to specify the width and height of the scanning rectangle in pixels.
         * The app will try to honor these, but will clamp them to the size of the preview frame.
         * You should specify both or neither, and pass the size as an int.
         */
        public static final String WIDTH = "SCAN_WIDTH";
        public static final String HEIGHT = "SCAN_HEIGHT";

        /**
         * Prompt to show on-screen when scanning by intent. Specified as a {@link String}.
         */
        public static final String PROMPT_MESSAGE = "PROMPT_MESSAGE";

        /**
         * Setting this to false will not save scanned codes in the history. Specified as a {@code boolean}.
         */
        public static final String SAVE_HISTORY = "SAVE_HISTORY";

        private Scan() {
        }
    }

    /**
     * Constants related to the scan history and retrieving history items.
     */
    public static final class History {

        public static final String ITEM_NUMBER = "ITEM_NUMBER";

        private History() {
        }
    }

    // Not the best place for this, but, better than a new class
    // Should be FLAG_ACTIVITY_NEW_DOCUMENT in API 21+.
    // Defined once here because the current value is deprecated, so generates just one warning
    public static final int FLAG_NEW_DOC = Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
}
