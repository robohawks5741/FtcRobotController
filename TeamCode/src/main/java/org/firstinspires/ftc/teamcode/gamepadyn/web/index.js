const clarifierElement = document.getElementById('clarifier');
const gpadStatsElement = document.getElementById('agpstatus');

/** @type {Object<number, Gamepad]>} */
const gamepads = {};
/** @type {number | null} */
var activeGamepadIndex = null;
/** @type {boolean} */
var isGamepadEnabled = false;

// SEE THIS !!!
// https://w3c.github.io/gamepad/#remapping

/** @function */
const requestFrame = window?.requestAnimationFrame || window?.mozRequestAnimationFrame || window?.webkitRequestAnimationFrame || window?.msRequestAnimationFrame;
if (!requestFrame) {
    throw new Error("What browser are you using?! This site needs window.requestAnimationFrame to function!");
}

class GamepadBindingElement extends HTMLDivElement {
    /** @private @type {Gamepad} */
    _gamepad;

    /** @type {Gamepad} */
    get gamepad() { return this._gamepad; }
    /** @param {Gamepad|number} */
    set gamepad(gid) {
        let t = typeof gid;
        switch (t) {
            case 'number': {
                let g = window.navigator.getGamepads()?.[gid];
                if (g) this._gamepad = g; else {
                    throw new Error(`Invalid gamepad index "${gid}"`);
                }
                break;
            }
            case 'object': {
                // t?.__proto__?.name
                // TODO: set gamepad via object
                console.warn("Cannot set gamepad via Gamepad object yet! Implement me later. Returning null.");
                return null;
            }
            default: {
                throw new Error('Invalid type for gamepad! Needs "Gamepad" object or a gamepad index (number)!');
            }
        }
    }

    /** @public @ */
    update() { }

    constructor() {
        super();
        let gid = this.getAttribute('gamepad-index');
        if (gid) this.gamepad = gid;
        return;
    }
}

class GamepadVisualizerElement extends GamepadBindingElement {
    static htmlContent = `<link rel="stylesheet" href="visualizer.css">
<h1>Gamepad Input Mapper</h1>
<div id="gpadgrid">
    <div class="p4d nosel" id="dpad">
        <div class="b" id="du">
            <span>
                <svg xmlns="http://www.w3.org/2000/svg" height="48" width="48">
                    <path
                        d="M24 31.75q.75 0 1.3-.525t.55-1.275V23.1l2.1 2.15q.5.55 1.25.525.75-.025 1.3-.575.5-.5.5-1.25t-.5-1.3l-5.25-5.2q-.25-.25-.575-.4-.325-.15-.675-.15-.4 0-.7.15-.3.15-.55.4L17.5 22.7q-.55.55-.525 1.275.025.725.575 1.225.5.55 1.25.55t1.3-.55l2.1-2.1v6.85q0 .8.525 1.3t1.275.5Zm0 13.05q-4.4 0-8.2-1.6-3.8-1.6-6.6-4.4-2.8-2.8-4.4-6.6-1.6-3.8-1.6-8.2 0-4.4 1.6-8.2Q6.4 12 9.2 9.2q2.8-2.8 6.6-4.4 3.8-1.6 8.2-1.6 4.4 0 8.2 1.6 3.8 1.6 6.6 4.4 2.8 2.8 4.4 6.6 1.6 3.8 1.6 8.2 0 4.4-1.6 8.2-1.6 3.8-4.4 6.6-2.8 2.8-6.6 4.4-3.8 1.6-8.2 1.6Zm0-3.65q7.3 0 12.225-4.925T41.15 24q0-7.3-4.925-12.225T24 6.85q-7.3 0-12.225 4.925T6.85 24q0 7.3 4.925 12.225T24 41.15ZM24 24Z" />
                </svg>
            </span>
        </div>
        <div class="b" id="dr">
            <span>
                <svg xmlns="http://www.w3.org/2000/svg" height="48" width="48">
                    <path
                        d="m25.3 30.45 5.2-5.2q.5-.5.5-1.25t-.5-1.3l-5.25-5.25q-.5-.5-1.25-.475-.75.025-1.25.525-.55.55-.55 1.3t.55 1.25l2.1 2.1h-6.9q-.75 0-1.25.55t-.5 1.3q0 .75.525 1.275.525.525 1.275.525h6.85l-2.15 2.15q-.55.5-.525 1.25.025.75.575 1.25.5.55 1.25.55t1.3-.55ZM24 44.8q-4.4 0-8.2-1.6-3.8-1.6-6.6-4.4-2.8-2.8-4.4-6.6-1.6-3.8-1.6-8.2 0-4.4 1.6-8.2Q6.4 12 9.2 9.2q2.8-2.8 6.6-4.4 3.8-1.6 8.2-1.6 4.4 0 8.2 1.6 3.8 1.6 6.6 4.4 2.8 2.8 4.4 6.6 1.6 3.8 1.6 8.2 0 4.4-1.6 8.2-1.6 3.8-4.4 6.6-2.8 2.8-6.6 4.4-3.8 1.6-8.2 1.6Zm0-3.65q7.3 0 12.225-4.925T41.15 24q0-7.3-4.925-12.225T24 6.85q-7.3 0-12.225 4.925T6.85 24q0 7.3 4.925 12.225T24 41.15ZM24 24Z" />
                </svg>
            </span>
        </div>
        <div class="b" id="dd">
            <span>
                <svg xmlns="http://www.w3.org/2000/svg" height="48" width="48">
                    <path
                        d="M24 31.05q.35 0 .675-.125.325-.125.575-.425l5.25-5.2q.5-.55.5-1.275 0-.725-.5-1.275-.55-.5-1.3-.5t-1.25.5l-2.1 2.1V18q0-.75-.55-1.275-.55-.525-1.3-.525t-1.275.55q-.525.55-.525 1.3v6.8l-2.15-2.1q-.55-.55-1.275-.525-.725.025-1.225.525-.55.55-.55 1.3t.55 1.25l5.2 5.2q.25.3.575.425.325.125.675.125Zm0 13.75q-4.4 0-8.2-1.6-3.8-1.6-6.6-4.4-2.8-2.8-4.4-6.6-1.6-3.8-1.6-8.2 0-4.4 1.6-8.2Q6.4 12 9.2 9.2q2.8-2.8 6.6-4.4 3.8-1.6 8.2-1.6 4.4 0 8.2 1.6 3.8 1.6 6.6 4.4 2.8 2.8 4.4 6.6 1.6 3.8 1.6 8.2 0 4.4-1.6 8.2-1.6 3.8-4.4 6.6-2.8 2.8-6.6 4.4-3.8 1.6-8.2 1.6Zm0-3.65q7.3 0 12.225-4.925T41.15 24q0-7.3-4.925-12.225T24 6.85q-7.3 0-12.225 4.925T6.85 24q0 7.3 4.925 12.225T24 41.15ZM24 24Z" />
                </svg>
            </span>
        </div>
        <div class="b" id="dl">
            <span>
                <svg xmlns="http://www.w3.org/2000/svg" height="48" width="48">
                    <path
                        d="M22.8 30.5q.5.55 1.225.525.725-.025 1.275-.575.5-.5.5-1.25t-.5-1.3l-2.1-2.1h6.85q.75 0 1.275-.525.525-.525.525-1.275 0-.75-.55-1.3t-1.3-.55h-6.8L25.35 20q.5-.5.475-1.225-.025-.725-.525-1.275-.55-.5-1.3-.5t-1.25.5l-5.2 5.2Q17 23.25 17 24t.55 1.25ZM24 44.8q-4.4 0-8.2-1.6-3.8-1.6-6.6-4.4-2.8-2.8-4.4-6.6-1.6-3.8-1.6-8.2 0-4.4 1.6-8.2Q6.4 12 9.2 9.2q2.8-2.8 6.6-4.4 3.8-1.6 8.2-1.6 4.4 0 8.2 1.6 3.8 1.6 6.6 4.4 2.8 2.8 4.4 6.6 1.6 3.8 1.6 8.2 0 4.4-1.6 8.2-1.6 3.8-4.4 6.6-2.8 2.8-6.6 4.4-3.8 1.6-8.2 1.6Zm0-3.65q7.3 0 12.225-4.925T41.15 24q0-7.3-4.925-12.225T24 6.85q-7.3 0-12.225 4.925T6.85 24q0 7.3 4.925 12.225T24 41.15ZM24 24Z" />
                </svg>
            </span>
        </div>
    </div>
    <!-- <label>DPAD</label> -->
    <div class="b stick nosel" id="lstick">
        <label>LS</label>
    </div>
    <div id="sbtns">
        <h1>hi I am the special buttons</h1>
    </div>
    <div class="b stick nosel" id="rstick">
        <label>RS</label>
    </div>
    <div class="p4d nosel" id="facebtns">
        <div class="b" id="fa"><span>A</span></div>
        <div class="b" id="fb"><span>B</span></div>
        <div class="b" id="fx"><span>X</span></div>
        <div class="b" id="fy"><span>Y</span></div>
    </div>
</div>`;
    get tagName() { return 'GAMEPAD-VISUALIZER'; };
    get nodeName() { return this.tagName; };
    constructor() {
        super();
        // this.appendChild();
        // this.shadowRoot
        const shadow = this.attachShadow({
            mode: 'closed'
        });
        shadow.innerHTML += GamepadVisualizerElement.htmlContent;
    }
};

class GamepadStatusElement extends GamepadBindingElement {
    static htmlContent = `<link rel="stylesheet" href="status.css">
<div id=''>
</div>`;
    get tagName() { return 'GAMEPAD-STATUS'; };
    get nodeName() { return this.tagName; };

    constructor() {
        super();
        let gid = this.getAttribute('gamepad-index');
        if (gid) this.gamepad = gid;
        // this.appendChild();
        // this.shadowRoot

        const shadow = this.attachShadow({
            mode: 'closed'
        });
        shadow.appendChild(lnk);
        shadow.innerHTML += GamepadVisualizerElement.htmlContent;
    }
};

customElements.define(GamepadStatusElement.prototype.tagName.toLowerCase(), GamepadStatusElement, { extends: 'div' });
customElements.define(GamepadVisualizerElement.prototype.tagName.toLowerCase(), GamepadVisualizerElement, { extends: 'div' });

/**
 * @param {GamepadEvent} event 
 * @param {boolean} connecting 
 */
function gamepadHandler(event, connecting) {
    console.log('Gamepad Event', event)

    /** @type {Gamepad} */
    const gamepad = event.gamepad;

    if (connecting) {
        gamepads[gamepad.index] = gamepad;
        if (isGamepadEnabled) {
            activeGamepadIndex = gamepad;
        }
    } else {
        delete gamepads[gamepad.index];
    }

    console.log("Gamepad Keys:", Object.keys(gamepads));

    let hasGamepad = (Object.keys(gamepads).length > 0);

    if (connecting) {
        gamepads[gamepad.index] = gamepad;
        if (isGamepadEnabled) {
            activeGamepadIndex = gamepad;
        }
    } else {
        if (event.gamepad.index === activeGamepadIndex) {
            if (hasGamepad) {
                
            }
        }
    }

    if (isGamepadEnabled) {
        activeGamepadIndex = event
    }
}

function queryGamepads() {
    let gpads = Object.keys(gamepads);
    let children = Array.from(gpadStatsElement.children);
    for (let i = 0; i < gpads.length; i++) {

        clarifierElement.innerHTML += `${gamepad.id} (controller #${gamepad.index})`;

        for (let i = 0; i < gamepad.buttons.length; i++) {
            clarifierElement.innerHTML += `<span>button ${i}</span>`;
        }

        for (let i = 0; i < gamepad.axes.length; i++) {
            clarifierElement.innerHTML += `<span>axis ${i}</span>`;
        }
    }

    if (isGamepadEnabled && activeGamepadIndex !== null) requestFrame(queryGamepads);
    return;
}

window.addEventListener("gamepadconnected", ev => { gamepadHandler(ev, true); });
window.addEventListener("gamepaddisconnected", ev => { gamepadHandler(ev, false); });
