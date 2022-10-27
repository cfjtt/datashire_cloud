/*
 * noVNC: HTML5 VNC client
 * Copyright (C) 2012 Joel Martin
 * Copyright (C) 2013 Samuel Mannehed for Cendio AB
 * Licensed under MPL 2.0 or any later version (see LICENSE.txt)
 */

import * as Log from '../util/logging.js';
import { stopEvent } from '../util/events.js';
import * as KeyboardUtil from "./util.js";
import KeyTable from "./keysym.js";
import * as browser from "../util/browser.js";
import keysyms from "./keysymdef.js";
import DOMKeyTable from "./domkeytable.js";
import fixedkeys from "./fixedkeys.js";

//
// Keyboard event handler
//

export default function Keyboard(target) {
    this._target = target || null;

    this._keyDownList = {};         // List of depressed keys
                                    // (even if they are happy)
    this._pendingKey = null;        // Key waiting for keypress
    this._altGrArmed = false;       // Windows AltGr detection

    // keep these here so we can refer to them later
    this._eventHandlers = {
        'keyup': this._handleKeyUp.bind(this),
        'keydown': this._handleKeyDown.bind(this),
        'keypress': this._handleKeyPress.bind(this),
        'blur': this._allKeysUp.bind(this),
        'checkalt': this._checkAlt.bind(this),
    };
};

Keyboard.prototype = {
    // ===== EVENT HANDLERS =====

    onkeyevent: function () {},     // Handler for key press/release

    // ===== PRIVATE METHODS =====

    _sendKeyEvent: function (keysym, code, down) {
        if (down) {
            this._keyDownList[code] = keysym;
        } else {
            // Do we really think this key is down?
            if (!(code in this._keyDownList)) {
                return;
            }
            delete this._keyDownList[code];
        }

        Log.Debug("onkeyevent " + (down ? "down" : "up") +
            ", keysym: " + keysym, ", code: " + code);
        this.onkeyevent(keysym, code, down);
    },

    _getKeyCode: function (e) {
        var code = KeyboardUtil.getKeycode(e);
        if (code !== 'Unidentified') {
            return code;
        }

        // Unstable, but we don't have anything else to go on
        // (don't use it for 'keypress' events thought since
        // WebKit sets it to the same as charCode)
        if (e.keyCode && (e.type !== 'keypress')) {
            // 229 is used for composition events
            if (e.keyCode !== 229) {
                return 'Platform' + e.keyCode;
            }
        }

        // A precursor to the final DOM3 standard. Unfortunately it
        // is not layout independent, so it is as bad as using keyCode
        if (e.keyIdentifier) {
            // Non-character key?
            if (e.keyIdentifier.substr(0, 2) !== 'U+') {
                return e.keyIdentifier;
            }

            var codepoint = parseInt(e.keyIdentifier.substr(2), 16);
            var char = String.fromCharCode(codepoint);
            // Some implementations fail to uppercase the symbols
            char = char.toUpperCase();

            return 'Platform' + char.charCodeAt();
        }

        return 'Unidentified';
    },
    _getKey:function(evt) {
        // Are we getting a proper key value?
        if (evt.key !== undefined) {
            // IE and Edge use some ancient version of the spec
            // https://developer.microsoft.com/en-us/microsoft-edge/platform/issues/8860571/
            switch (evt.key) {
                case 'Spacebar': return ' ';
                case 'Esc': return 'Escape';
                case 'Scroll': return 'ScrollLock';
                case 'Win': return 'Meta';
                case 'Apps': return 'ContextMenu';
                case 'Up': return 'ArrowUp';
                case 'Left': return 'ArrowLeft';
                case 'Right': return 'ArrowRight';
                case 'Down': return 'ArrowDown';
                case 'Del': return 'Delete';
                case 'Divide': return '/';
                case 'Multiply': return '*';
                case 'Subtract': return '-';
                case 'Add': return '+';
                case 'Decimal': return evt.char;
            }

            // Mozilla isn't fully in sync with the spec yet
            switch (evt.key) {
                case 'OS': return 'Meta';
            }

            // iOS leaks some OS names
            switch (evt.key) {
                case 'UIKeyInputUpArrow': return 'ArrowUp';
                case 'UIKeyInputDownArrow': return 'ArrowDown';
                case 'UIKeyInputLeftArrow': return 'ArrowLeft';
                case 'UIKeyInputRightArrow': return 'ArrowRight';
                case 'UIKeyInputEscape': return 'Escape';
            }

            // IE and Edge have broken handling of AltGraph so we cannot
            // trust them for printable characters
            if ((evt.key.length !== 1) || (!browser.isIE() && !browser.isEdge())) {
                if(typeof this._keyDownList['ShiftLeft'] != 'undefined' || typeof this._keyDownList['ShiftRight'] != 'undefined'){
                    switch (evt.keyCode){
                        case 49:
                            return '!';
                            break;
                        case 50:
                            return '@';
                            break;
                        case 51:
                            return '#';
                            break;
                        case 52:
                            return '$';
                            break;
                        case 53:
                            return '%';
                            break;
                        case 54:
                            return '^';
                            break;
                        case 55:
                            return '&';
                            break;
                        case 56:
                            return '*';
                            break;
                        case 57:
                            return '(';
                            break;
                        case 48:
                            return ')';
                            break;
                        case 187:
                            return '+';
                            break;
                        case 219:
                            return '{';
                            break;
                        case 221:
                            return '}';
                            break;
                        case 220:
                            return '|';
                            break;
                        case 186:
                            return ':';
                            break;
                        case 222:
                            return '"';
                            break;
                        case 188:
                            return '<';
                            break;
                        case 189:
                            return  '_';
                            break;
                        case 190:
                            return '>';
                            break;
                        case 191:
                            return '?';
                            break;
                        case 192:
                            return '~';
                            break;
                        case 65:
                            return 'A';
                            break;
                        case 66:
                            return 'B';
                            break;
                        case 67:
                            return 'C';
                            break;
                        case 68:
                            return 'D';
                            break;
                        case 69:
                            return 'E';
                            break;
                        case 70:
                            return 'F';
                            break;
                        case 71:
                            return 'G';
                            break;
                        case 72:
                            return 'H';
                            break;
                        case 73:
                            return 'I';
                            break;
                        case 74:
                            return 'J';
                            break;
                        case 75:
                            return 'K';
                            break;
                        case 76:
                            return 'L';
                            break;
                        case 77:
                            return 'M';
                            break;
                        case 78:
                            return 'N';
                            break;
                        case 79:
                            return 'O';
                            break;
                        case 80:
                            return 'P';
                            break;
                        case 81:
                            return 'Q';
                            break;
                        case 82:
                            return 'R';
                            break;
                        case 83:
                            return 'S';
                            break;
                        case 84:
                            return 'T';
                            break;
                        case 85:
                            return 'U';
                            break;
                        case 86:
                            return 'V';
                            break;
                        case 87:
                            return 'W';
                            break;
                        case 88:
                            return 'X';
                            break;
                        case 89:
                            return 'Y';
                            break;
                        case 90:
                            return 'Z';
                            break;


                    }
                    return evt.key;
                }
                // switch (evt.keyCode){
                //     case 65:
                //         return 'A';
                //         break;
                //     case 66:
                //         return 'B';
                //         break;
                //     case 67:
                //         return 'C';
                //         break;
                //     case 68:
                //         return 'D';
                //         break;
                //     case 69:
                //         return 'E';
                //         break;
                //     case 70:
                //         return 'F';
                //         break;
                //     case 71:
                //         return 'G';
                //         break;
                //     case 72:
                //         return 'H';
                //         break;
                //     case 73:
                //         return 'I';
                //         break;
                //     case 74:
                //         return 'J';
                //         break;
                //     case 75:
                //         return 'K';
                //         break;
                //     case 76:
                //         return 'L';
                //         break;
                //     case 77:
                //         return 'M';
                //         break;
                //     case 78:
                //         return 'N';
                //         break;
                //     case 79:
                //         return 'O';
                //         break;
                //     case 80:
                //         return 'P';
                //         break;
                //     case 81:
                //         return 'Q';
                //         break;
                //     case 82:
                //         return 'R';
                //         break;
                //     case 83:
                //         return 'S';
                //         break;
                //     case 84:
                //         return 'T';
                //         break;
                //     case 85:
                //         return 'U';
                //         break;
                //     case 86:
                //         return 'V';
                //         break;
                //     case 87:
                //         return 'W';
                //         break;
                //     case 88:
                //         return 'X';
                //         break;
                //     case 89:
                //         return 'Y';
                //         break;
                //     case 90:
                //         return 'Z';
                //         break;
                // }
                return evt.key;
            }
        }

        // Try to deduce it based on the physical key
        var code = KeyboardUtil.getKeycode(evt);
        if (code in fixedkeys) {
            return fixedkeys[code];
        }

        // If that failed, then see if we have a printable character
        if (evt.charCode) {
            return String.fromCharCode(evt.charCode);
        }

        // At this point we have nothing left to go on
        return 'Unidentified';
    },
    _getKeysym:function(evt){
        var key = this._getKey(evt);
        if (key === 'Unidentified') {
            return null;
        }
        // First look up special keys
        if (key in DOMKeyTable) {
            var location = evt.location;

            // Safari screws up location for the right cmd key
            if ((key === 'Meta') && (location === 0)) {
                location = 2;
            }

            if ((location === undefined) || (location > 3)) {
                location = 0;
            }
            return DOMKeyTable[key][location];
        }

        // Now we need to look at the Unicode symbol instead

        var codepoint;

        // Special key? (FIXME: Should have been caught earlier)
        if (key.length !== 1) {
            return null;
        }

        codepoint = key.charCodeAt();
        if (codepoint) {
            return keysyms.lookup(codepoint);
        }

        return null;
    },
    _handleKeyDown: function (e) {
        console.log("keydown");
        console.log(e);
        var code = this._getKeyCode(e);
        var keysym = this._getKeysym(e);
        console.log(code,keysym);
        // Windows doesn't have a proper AltGr, but handles it using
        // fake Ctrl+Alt. However the remote end might not be Windows,
        // so we need to merge those in to a single AltGr event. We
        // detect this case by seeing the two key events directly after
        // each other with a very short time between them (<50ms).
        if (this._altGrArmed) {
            this._altGrArmed = false;
            clearTimeout(this._altGrTimeout);

            if ((code === "AltRight") &&
                ((e.timeStamp - this._altGrCtrlTime) < 50)) {
                // FIXME: We fail to detect this if either Ctrl key is
                //        first manually pressed as Windows then no
                //        longer sends the fake Ctrl down event. It
                //        does however happily send real Ctrl events
                //        even when AltGr is already down. Some
                //        browsers detect this for us though and set the
                //        key to "AltGraph".
                keysym = KeyTable.XK_ISO_Level3_Shift;
            } else {
                this._sendKeyEvent(KeyTable.XK_Control_L, "ControlLeft", true);
            }
        }

        // We cannot handle keys we cannot track, but we also need
        // to deal with virtual keyboards which omit key info
        // (iOS omits tracking info on keyup events, which forces us to
        // special treat that platform here)
        if ((code === 'Unidentified') || browser.isIOS()) {
            if (keysym) {
                // If it's a virtual keyboard then it should be
                // sufficient to just send press and release right
                // after each other
                this._sendKeyEvent(keysym, code, true);
                this._sendKeyEvent(keysym, code, false);
            }

            stopEvent(e);
            return;
        }

        // Alt behaves more like AltGraph on macOS, so shuffle the
        // keys around a bit to make things more sane for the remote
        // server. This method is used by RealVNC and TigerVNC (and
        // possibly others).
        if (browser.isMac()) {
            switch (keysym) {
                case KeyTable.XK_Super_L:
                    keysym = KeyTable.XK_Alt_L;
                    break;
                case KeyTable.XK_Super_R:
                    keysym = KeyTable.XK_Super_L;
                    break;
                case KeyTable.XK_Alt_L:
                    keysym = KeyTable.XK_Mode_switch;
                    break;
                case KeyTable.XK_Alt_R:
                    keysym = KeyTable.XK_ISO_Level3_Shift;
                    break;
            }
        }

        // Is this key already pressed? If so, then we must use the
        // same keysym or we'll confuse the server
        if (code in this._keyDownList) {
            keysym = this._keyDownList[code];
        }

        // macOS doesn't send proper key events for modifiers, only
        // state change events. That gets extra confusing for CapsLock
        // which toggles on each press, but not on release. So pretend
        // it was a quick press and release of the button.
        if (browser.isMac() && (code === 'CapsLock')) {
            this._sendKeyEvent(KeyTable.XK_Caps_Lock, 'CapsLock', true);
            this._sendKeyEvent(KeyTable.XK_Caps_Lock, 'CapsLock', false);
            stopEvent(e);
            return;
        }

        // If this is a legacy browser then we'll need to wait for
        // a keypress event as well
        // (IE and Edge has a broken KeyboardEvent.key, so we can't
        // just check for the presence of that field)
        if (!keysym && (!e.key || browser.isIE() || browser.isEdge())) {
            this._pendingKey = code;
            // However we might not get a keypress event if the key
            // is non-printable, which needs some special fallback
            // handling
            setTimeout(this._handleKeyPressTimeout.bind(this), 10, e);
            return;
        }

        this._pendingKey = null;
        stopEvent(e);

        // Possible start of AltGr sequence? (see above)
        if ((code === "ControlLeft") && browser.isWindows() &&
            !("ControlLeft" in this._keyDownList)) {
            this._altGrArmed = true;
            this._altGrTimeout = setTimeout(this._handleAltGrTimeout.bind(this), 100);
            this._altGrCtrlTime = e.timeStamp;
            return;
        }

        this._sendKeyEvent(keysym, code, true);
    },

    // Legacy event for browsers without code/key
    _handleKeyPress: function (e) {
        stopEvent(e);
        // Are we expecting a keypress?
        if (this._pendingKey === null) {
            return;
        }

        var code = this._getKeyCode(e);
        var keysym = KeyboardUtil.getKeysym(e);

        console.log("keypress:"+code+"----"+keysym);
        console.log(e);
        // The key we were waiting for?
        if ((code !== 'Unidentified') && (code != this._pendingKey)) {
            return;
        }

        code = this._pendingKey;
        this._pendingKey = null;

        if (!keysym) {
            Log.Info('keypress with no keysym:', e);
            return;
        }

        this._sendKeyEvent(keysym, code, true);
    },
    _handleKeyPressTimeout: function (e) {
        // Did someone manage to sort out the key already?
        if (this._pendingKey === null) {
            return;
        }

        var code, keysym;

        code = this._pendingKey;
        this._pendingKey = null;

        // We have no way of knowing the proper keysym with the
        // information given, but the following are true for most
        // layouts
        if ((e.keyCode >= 0x30) && (e.keyCode <= 0x39)) {
            // Digit
            keysym = e.keyCode;
        } else if ((e.keyCode >= 0x41) && (e.keyCode <= 0x5a)) {
            // Character (A-Z)
            var char = String.fromCharCode(e.keyCode);
            // A feeble attempt at the correct case
            if (e.shiftKey)
                char = char.toUpperCase();
            else
                char = char.toLowerCase();
            keysym = char.charCodeAt();
        } else {
            // Unknown, give up
            keysym = 0;
        }

        this._sendKeyEvent(keysym, code, true);
    },

    _handleKeyUp: function (e) {
        stopEvent(e);
        var code = this._getKeyCode(e);
        console.log("keyup");
        console.log(e);
        // We can't get a release in the middle of an AltGr sequence, so
        // abort that detection
        if (this._altGrArmed) {
            this._altGrArmed = false;
            clearTimeout(this._altGrTimeout);
            this._sendKeyEvent(KeyTable.XK_Control_L, "ControlLeft", true);
        }

        // See comment in _handleKeyDown()
        if (browser.isMac() && (code === 'CapsLock')) {
            this._sendKeyEvent(KeyTable.XK_Caps_Lock, 'CapsLock', true);
            this._sendKeyEvent(KeyTable.XK_Caps_Lock, 'CapsLock', false);
            return;
        }

        this._sendKeyEvent(this._keyDownList[code], code, false);
    },

    _handleAltGrTimeout: function () {
        this._altGrArmed = false;
        clearTimeout(this._altGrTimeout);
        this._sendKeyEvent(KeyTable.XK_Control_L, "ControlLeft", true);
    },

    _allKeysUp: function () {
        Log.Debug(">> Keyboard.allKeysUp");
        for (var code in this._keyDownList) {
            this._sendKeyEvent(this._keyDownList[code], code, false);
        };
        Log.Debug("<< Keyboard.allKeysUp");
    },

    // Firefox Alt workaround, see below
    _checkAlt: function (e) {
        if (e.altKey) {
            return;
        }

        let target = this._target;
        let downList = this._keyDownList;
        ['AltLeft', 'AltRight'].forEach(function (code) {
            if (!(code in downList)) {
                return;
            }

            let event = new KeyboardEvent('keyup',
                { key: downList[code],
                    code: code });
            target.dispatchEvent(event);
        });
    },

    // ===== PUBLIC METHODS =====

    grab: function () {
        //Log.Debug(">> Keyboard.grab");
        var c = this._target;

        c.addEventListener('keydown', this._eventHandlers.keydown);
        c.addEventListener('keyup', this._eventHandlers.keyup);
        c.addEventListener('keypress', this._eventHandlers.keypress);

        // Release (key up) if window loses focus
        window.addEventListener('blur', this._eventHandlers.blur);

        // Firefox has broken handling of Alt, so we need to poll as
        // best we can for releases (still doesn't prevent the menu
        // from popping up though as we can't call preventDefault())
        if (browser.isWindows() && browser.isFirefox()) {
            let handler = this._eventHandlers.checkalt;
            ['mousedown', 'mouseup', 'mousemove', 'wheel',
                'touchstart', 'touchend', 'touchmove',
                'keydown', 'keyup'].forEach(function (type) {
                document.addEventListener(type, handler,
                    { capture: true,
                        passive: true });
            });
        }

        //Log.Debug("<< Keyboard.grab");
    },

    ungrab: function () {
        //Log.Debug(">> Keyboard.ungrab");
        var c = this._target;

        if (browser.isWindows() && browser.isFirefox()) {
            let handler = this._eventHandlers.checkalt;
            ['mousedown', 'mouseup', 'mousemove', 'wheel',
                'touchstart', 'touchend', 'touchmove',
                'keydown', 'keyup'].forEach(function (type) {
                document.removeEventListener(type, handler);
            });
        }

        c.removeEventListener('keydown', this._eventHandlers.keydown);
        c.removeEventListener('keyup', this._eventHandlers.keyup);
        c.removeEventListener('keypress', this._eventHandlers.keypress);
        window.removeEventListener('blur', this._eventHandlers.blur);

        // Release (key up) all keys that are in a down state
        this._allKeysUp();

        //Log.Debug(">> Keyboard.ungrab");
    },
};
