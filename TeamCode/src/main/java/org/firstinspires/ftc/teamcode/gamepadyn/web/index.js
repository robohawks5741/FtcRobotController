const clarifierElement = document.getElementById('clarifier');

const gamepads = {};

// SEE THIS !!!
// https://w3c.github.io/gamepad/#remapping

/**
 * @param {GamepadEvent} event 
 * @param {boolean} connecting 
 */
function gamepadHandler(event, connecting) {
    /** @type {Gamepad} */
    const gamepad = event.gamepad;

    if (connecting) {
        gamepads[gamepad.index] = gamepad;
    } else {
        delete gamepads[gamepad.index];
    }

    clarifierElement.innerHTML += `${gamepad.id} (controller #${gamepad.index})`;

    for (let i = 0; i < gamepad.buttons.length; i++) {
        clarifierElement.innerHTML += `<span>button ${i}</span>`;
    }

    for (let i = 0; i < gamepad.axes.length; i++) {
        clarifierElement.innerHTML += `<span>axis ${i}</span>`;
    }
}

window.addEventListener("gamepadconnected", ev => { gamepadHandler(ev, true); }, false);
window.addEventListener("gamepaddisconnected", ev => { gamepadHandler(ev, false); }, false);
