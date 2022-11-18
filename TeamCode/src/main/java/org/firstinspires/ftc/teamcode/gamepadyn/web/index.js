/** @type {GamepadVisualizerElement} */
const bigVisualizer = document.getElementById('bigvis');
const clarifierElement = document.getElementById('clarifier');
const gpadStatsElement = document.getElementById('agpstatus');

/** @type {Object<number, Gamepad]>} */
const gamepads = {};
/** @type {number | null} */
var activeGamepadIndex = null;

/** @function */
const requestFrame = window?.requestAnimationFrame || window?.mozRequestAnimationFrame || window?.webkitRequestAnimationFrame || window?.msRequestAnimationFrame;
if (!requestFrame) {
    throw new Error("What browser are you using?! This site needs window.requestAnimationFrame to function!");
}

/** Clamps a value between -1 and 1. */
function norm(x) {
    return Math.max(-1, Math.min(1, x));
}

/**
 * Not a typo.
 */
function cerealizer() {

}

/** @typedef {{x: number, y: number}} Vec2d A 2-dimensional vector. */

/**
 * @template {any} [TButton=boolean] The data type of buttons (defaults to `boolean`)
 * @template {any} [TStick=Vec2d] The data type of the two sticks (defaults to {@linkcode Vec2d})
 * @typedef {object} StandardGamepadMap
 * @property {object} dpad
 * @property {TButton} dpad.up
 * @property {TButton} dpad.down
 * @property {TButton} dpad.left
 * @property {TButton} dpad.right
 * @property {object} face
 * @property {TButton} face.up
 * @property {TButton} face.down
 * @property {TButton} face.left
 * @property {TButton} face.right
 * @property {object} stick
 * @property {TStick} stick.l
 * @property {TStick} stick.r
 * @property {TButton} stick.lbutton
 * @property {TButton} stick.rbutton
 * @property {object} trigger
 * @property {TButton} trigger.l
 * @property {TButton} trigger.r
 * @property {object} bumper
 * @property {TButton} bumper.l
 * @property {TButton} bumper.r
 */

/**
 * @classdesc An HTML element with a shadow DOM that relates to a gamepad. Use {@linkcode setGamepad()} to assign a gamepad.
 * @see {@link https://w3c.github.io/gamepad/#remapping W3C standard gamepad mapping}
 */
class GamepadBindingElement extends HTMLElement {
    /** @protected  @type {Gamepad | undefined} */
    _gamepad;

    /** @readonly @type {Gamepad | undefined} */
    get gamepad() { return this._gamepad; }

    /**
     * @param {Gamepad | number | string} gid The gamepad.
     * @todo Allowing setting gamepad via a Gamepad object
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
                console.warn("Cannot set gamepad via Gamepad object yet! Implement me later. Returning.");
                return;
            }
            default: {
                throw new Error('Invalid type for gamepad! Needs "Gamepad" object or a gamepad index (number)!');
            }
        }
        this.setAttribute('gamepad-index', this.gamepad.id);
    }

    /** @readonly @type {StandardGamepadMap<boolean, Vec2d>} */
    get standard() {
        if (this._gamepad == null) return null;
        let gp = this._gamepad;
        return {
            dpad: {
                up: gp.buttons[12].pressed,
                down: gp.buttons[13].pressed,
                left: gp.buttons[14].pressed,
                right: gp.buttons[15].pressed
            },
            face: {
                up: gp.buttons[3].pressed,
                down: gp.buttons[0].pressed,
                left: gp.buttons[2].pressed,
                right: gp.buttons[1].pressed
            },
            stick: {
                l: { x: gp.axes[0], y: gp.axes[1] },
                r: { x: gp.axes[2], y: gp.axes[3] }
            }
        };
    }

    /** @public */
    update() { return };

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
    <div class="b stick nosel" id="sl">
        <canvas class="stickrend" width="128" height="128"></canvas>
        <label>LS</label>
    </div>
    <div id="sbtns">
        <h1>&lt;temp&gt; Special Buttons</h1>
    </div>
    <div class="b stick nosel" id="sr">
        <canvas class="stickrend" width="128" height="128"></canvas>
        <label>RS</label>
    </div>
    <div class="p4d nosel" id="facebtns">
        <div class="b" id="fa"><span>A</span></div>
        <div class="b" id="fd"><span>B</span></div>
        <div class="b" id="fx"><span>X</span></div>
        <div class="b" id="fy"><span>Y</span></div>
    </div>
</div>`;
    get tagName() { return 'GAMEPAD-VISUALIZER'; };
    get nodeName() { return this.tagName; };

    /** @private @type {{canvas: HTMLCanvasElement, ctx: CanvasRenderingContext2D}} */
    _slRenderer = { canvas: null, ctx: null }
    /** @private @type {{canvas: HTMLCanvasElement, ctx: CanvasRenderingContext2D}} */
    _srRenderer = { canvas: null, ctx: null }
    /** @private @constant @readonly @type {CanvasRenderingContext2DSettings} */
    _rendererOptions = { alpha: true, desynchronized: true };

    /** @readonly @type {StandardGamepadMap<HTMLDivElement, HTMLDivElement>} */
    elementMap = {
        dpad: {
            up: undefined,
            down: undefined,
            left: undefined,
            right: undefined,
        },
        face: {
            up: undefined,
            down: undefined,
            left: undefined,
            right: undefined,
        },
        stick: {
            l: undefined,
            r: undefined
        }
    }

    /** @override */
    update() {
        const capt = this.standard;
        for (let i = 0; i < 2; i++) {
            let r = (i == 0 ? this._slRenderer : this._srRenderer);
            let s = capt.stick[(i == 0 ? 'l' : 'r')];
            r.ctx.clearRect(0, 0, 128, 128);
            if (Math.abs(s.x) >= 0.0625 || Math.abs(s.y) >= 0.0625) {
                r.ctx.strokeStyle = '#ff0000';
                r.ctx.lineCap = 'round';
                r.ctx.lineJoin = 'round';
                r.ctx.lineWidth = 8;
                r.ctx.fillStyle = '#0000'
                r.ctx.beginPath();
                r.ctx.moveTo(64, 64);
                // thx pythagoras
                let len = Math.max(0, Math.min(1, Math.sqrt(Math.pow(s.x, 2) + Math.pow(s.y, 2))));
                let a = Math.atan2(norm(s.y), norm(s.x));// * 180 / Math.PI;
                let x = Math.cos(a);
                let y = Math.sin(a);
                r.ctx.lineTo((x * len * 52) + 64, (y * len * 52) + 64);
                r.ctx.stroke();
                this.elementMap.stick[(i == 0 ? 'l' : 'r')].setAttribute('gpsl', 'true')
            } else {
                this.elementMap.stick[(i == 0 ? 'l' : 'r')].removeAttribute('gpsl');
            }
        }
    }

    constructor() {
        super();
        // this.appendChild();
        // this.shadowRoot
        const shadow = this.attachShadow({
            mode: 'closed'
        });
        shadow.innerHTML += GamepadVisualizerElement.htmlContent;

        this.elementMap.dpad.up = shadow.querySelector('#du');
        this.elementMap.dpad.down = shadow.querySelector('#dd');
        this.elementMap.dpad.left = shadow.querySelector('#dl');
        this.elementMap.dpad.right = shadow.querySelector('#dr');
        this.elementMap.face.up = shadow.querySelector('#fu');
        this.elementMap.face.down = shadow.querySelector('#fd');
        this.elementMap.face.left = shadow.querySelector('#fl');
        this.elementMap.face.right = shadow.querySelector('#fr');
        this.elementMap.stick.l = shadow.querySelector('#sl');
        this.elementMap.stick.r = shadow.querySelector('#sr');

        this._slRenderer.canvas = shadow.querySelector('#sl>.stickrend');
        this._srRenderer.canvas = shadow.querySelector('#sr>.stickrend');

        this._slRenderer.ctx = this._slRenderer.canvas.getContext('2d', this._rendererOptions);
        this._srRenderer.ctx = this._srRenderer.canvas.getContext('2d', this._rendererOptions);
        return;
    }
};
customElements.define(GamepadVisualizerElement.prototype.tagName.toLowerCase(), GamepadVisualizerElement);

class GamepadStatusElement extends GamepadBindingElement {
    get tagName() { return 'GAMEPAD-STATUS'; };
    get nodeName() { return this.tagName; };

    /** @protected @type {HTMLDivElement} */
    _container;

    /** @override */
    update() {
        if (typeof this.gamepad !== 'object') { this._container.innerHTML = `Gamepad Unavailable`; return; }
        if (!(this.gamepad?.connected)) {
            this.remove();
            return;
        }
        this._container.innerHTML = '';
        this._container.innerHTML = `&lt;PLACEHOLDER&gt; ID: "${this.gamepad?.id}"`;
        if (this.gamepad.mapping == 'standard') {
            for (let i = 0; i < this.gamepad.buttons.length; i++) {
                this._container.innerHTML += `<br>Button #${i} ${this.gamepad.buttons[i].value/*pressed ? "pressed" : "unpressed"*/}`;
            }
            for (let i = 0; i < this.gamepad.axes.length; i += 2) {
                this._container.innerHTML += `<br>Stick #${i} (${(i >= 2) ? 'right' : 'left'}) at ${this.gamepad.axes[i]}X ${this.gamepad.axes[i + 1]}Y`;
            }
        } else {
            console.warn("Using non-standard control mapping! This will be fine when we allow user-defined mapping but that\'s a stretch goal. Lots of code may not work.");
        }
    }

    constructor() {
        super();
        // TODO: listen to attribute changes
        let gid = this.getAttribute('gamepad-index');
        if (gid && gid?.length > 0) this.setGamepad(gid);
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
customElements.define(GamepadStatusElement.prototype.tagName.toLowerCase(), GamepadStatusElement);

/**
 * @param {GamepadEvent} event 
 */
function gamepadEventHandler(event) {
    let connecting = (event.type == 'gamepadconnected');
    console.log('Gamepad Event', event);

    /** @type {Gamepad} */
    const gamepad = event.gamepad;

    if (connecting) {
        console.log(`Connecting gamepad at index ${gamepad.index}`)
        /** @type {GamepadStatusElement} */
        let gs = document.createElement('gamepad-status');
        bigVisualizer.setGamepad(event.gamepad.index);
        gs.setGamepad(event.gamepad.index);
        console.log(gs);
        gpadStatsElement.appendChild(gs);
        gamepads[gamepad.index] = gamepad;
        if (activeGamepadIndex == null) {
            console.log(`Gamepad index now ${gamepad.index} (controller connected)`);
        } else {
            console.log(`Overriding gamepad index to ${gamepad.index} as it just connected`)
        }
        activeGamepadIndex = gamepad.index;
    } else {
        // disconnected
        console.log(`Disconnecting gamepad at index ${gamepad.index}`)
        delete gamepads[gamepad.index];
    }

    let hasGamepad = (Object.keys(gamepads).length > 0);

    if (!connecting) {
        // disconnected
        if (event.gamepad.index === activeGamepadIndex && hasGamepad) {
            let k = Object.keys(gamepads);
            // Lots of redundancy here, k will always be an array, i will always be a number.
            // If they aren't, this will never be called.
            // But, JavaScript has no such thing as redundancy, so I'm not getting rid of it.

            // Get the last controller
            let i = k?.[k.length - 1];
            if (typeof i == 'number') {
                console.log(`Gamepad index now ${i} (last available controller index)`);
                activeGamepadIndex = i;
            }
        }
        if (!hasGamepad) {
            // Gamepad disconnected, no alternatives, we sad now
            console.log(`Gamepad index now null (no gamepads?)`);
            activeGamepadIndex = null;
        }
    }

    updateGamepads();
    return;
}

/**
 * Updates the Gamepad Status elements
 */
async function updateGamepads() {
    // console.debug('updateGamepads()');
    /** @type {NodeListOf<GamepadStatusElement>} */
    let sElems = document.querySelectorAll('gamepad-status');
    for (let i = 0; i < sElems.length; i++) {
        sElems.item(i)?.update?.();
    }
    bigVisualizer?.update?.();
    if (activeGamepadIndex !== null) requestFrame(updateGamepads);
    return;
}

window.addEventListener('gamepadconnected', ev => { gamepadEventHandler(ev, true); });
window.addEventListener('gamepaddisconnected', ev => { gamepadEventHandler(ev, false); });
