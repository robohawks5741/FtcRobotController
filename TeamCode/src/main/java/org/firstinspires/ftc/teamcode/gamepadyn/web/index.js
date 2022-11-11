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

class GamepadBindingElement extends HTMLElement {
    /** @protected @type {Gamepad | undefined} */
    _gamepad;

    /** @readonly @type {Gamepad | undefined} */
    get gamepad() { return this._gamepad; }
    /**
     * @param {Gamepad | number | string} gid
     * @returns {void}
    */
    setGamepad(gid) {
        let t = typeof gid;
        switch (t) {
            case 'string':
                gid = (parseInt(gid) || parseFloat(gid));
            case 'number': {
                let g = window.navigator.getGamepads()?.[gid];
                if (g) this._gamepad = g; else {
                    throw new Error(`Invalid gamepad index "${gid}"`);
                }
                break;
            }
            case 'object': {
                // Object.getPrototypeOf(t)?.constructor?.name
                // TODO: set gamepad via object
                console.warn("Cannot set gamepad via Gamepad object yet! Implement me later. Returning.");
                return;
            }
            default: {
                throw new Error('Invalid type for gamepad! Needs "Gamepad" object or a gamepad index (number)!');
            }
        }
        this.setAttribute('gamepad-index', this.gamepad.id);
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

class GamepadVisualizerElement extends HTMLDivElement {
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
    get tagName() { return 'GAMEPAD-STATUS'; };
    get nodeName() { return this.tagName; };

    /** @protected @type {HTMLDivElement} */
    _container;

    /** @override */
    update() {
        if (!this.gamepad.connected) {
            this.remove();
            return;
        }
        this._container.innerHTML = '';
        this._container.innerHTML = `&lt;PLACEHOLDER&gt; ID: "${this.gamepad?.id}"`;
        if (this.gamepad.mapping == 'standard') {
            /*
        buttons[0] 	Bottom button in right cluster
        buttons[1] 	Right button in right cluster
        buttons[2] 	Left button in right cluster
        buttons[3] 	Top button in right cluster
        buttons[4] 	Top left front button
        buttons[5] 	Top right front button
        buttons[6] 	Bottom left front button
        buttons[7] 	Bottom right front button
        buttons[8] 	Left button in center cluster
        buttons[9] 	Right button in center cluster
        buttons[10] 	Left stick pressed button
        buttons[11] 	Right stick pressed button
        buttons[12] 	Top button in left cluster
        buttons[13] 	Bottom button in left cluster
        buttons[14] 	Left button in left cluster
        buttons[15] 	Right button in left cluster
        buttons[16] 	Center button in center cluster
        axes[0] 	Horizontal axis for left stick (negative left/positive right)
        axes[1] 	Vertical axis for left stick (negative up/positive down)
        axes[2] 	Horizontal axis for right stick (negative left/positive right)
        axes[3] 	Vertical axis for right stick (negative up/positive down) 
            */
            for (let i = 0; i < this.gamepad.buttons.length; i++) {
                this._container.innerHTML += `<br>Button #${i} ${this.gamepad.buttons[i].pressed ? "pressed" : "unpressed"}`;
            }
            for (let i = 0; i < this.gamepad.axes.length; i +=2 ) {
                this._container.innerHTML += `<br>Stick #${i} (${(i >= 2) ? 'right' : 'left'}) at ${this.gamepad.axes[i]}x${this.gamepad.axes[i+1]}y`;
            }
        } else {
            console.warn("Using non-standard control mapping! This will be fine when we allow user-defined mapping but that\'s a stretch goal. Lots of code may not work.");
        }
    }

    constructor() {
        super();
        // TODO: listen to attribute changes
        let gid = this.getAttribute('gamepad-index');
        if (gid) this.setGamepad(gid);
        // this.appendChild();
        // this.shadowRoot

        const shadow = this.attachShadow({
            mode: 'closed'
        });
        shadow.innerHTML += `<link rel="stylesheet" href="status.css">`;
        this._container = document.createElement('div');
        this._container.id = 'container';
        shadow.appendChild(this._container);
    }
};

customElements.define(GamepadStatusElement.prototype.tagName.toLowerCase(), GamepadStatusElement);//, { extends: 'div' });
customElements.define(GamepadVisualizerElement.prototype.tagName.toLowerCase(), GamepadVisualizerElement, { extends: 'div' });

/**
 * @param {GamepadEvent} event 
 */
function gamepadEventHandler(event) {
    let connecting = (event.type == 'gamepadconnected');
    console.log('Gamepad Event', event);

    /** @type {Gamepad} */
    const gamepad = event.gamepad;

    if (connecting) {
        /** @type {GamepadStatusElement} */
        let gs = document.createElement('gamepad-status');
        gs.gamepad = event.gamepad.index;
        gpadStatsElement.appendChild(gs);
        gamepads[gamepad.index] = gamepad;
        if (isGamepadEnabled) {
            activeGamepadIndex = gamepad;
        }
    } else {
        // disconnected
        delete gamepads[gamepad.index];
    }

    let hasGamepad = (Object.keys(gamepads).length > 0);

    if (connecting) {
        gamepads[gamepad.index] = gamepad;
        if (isGamepadEnabled) {
            console.log(`Updating gamepad index to ${gamepad.index} as it just connected`)
            activeGamepadIndex = gamepad.index;
        }
    } else {
        // disconnected
        if (event.gamepad.index === activeGamepadIndex) {
            if (hasGamepad) {
                let k = Object.keys(gamepads);
                // Lots of redundancy here, k will always be an array, i will always be a number.
                // If they aren't, this will never be called.
                // But, JavaScript has no such thing as redundancy, so I'm not getting rid of it.
                let i = k?.[k.length - 1];
                if (typeof i == 'number') activeGamepadIndex = i;
            }
        } else if (!hasGamepad) {
            // Gamepad disconnected, no alternatives, we sad now
            activeGamepadIndex = null;
            isGamepadEnabled = false;
        }
    }

    updateGamepads();
    return;
}

function updateGamepads() {
    /** @type {NodeListOf<GamepadStatusElement>} */
    let sElems = gpadStatsElement.querySelectorAll('gamepad-status');
    // this sounds super weird but it's not
    for (let i = 0; i < sElems.length; i++) {
        console.log(sElems.item(i));
        sElems.item(i).update();
    }

    if (isGamepadEnabled && activeGamepadIndex !== null) requestFrame(updateGamepads);
    return;
}

window.addEventListener("gamepadconnected", ev => { gamepadEventHandler(ev, true); });
window.addEventListener("gamepaddisconnected", ev => { gamepadEventHandler(ev, false); });
