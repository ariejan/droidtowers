function TestShip() {
	this.gameObject = new ScriptedGameObject();
	this.gameObject.useTexture("ships/scout2.png");
	this.gameObject.setVelocity(Math.random(), Math.random());
}

TestShip.prototype.update = function(timeDelta) {
	// out.println('timeDelta: '+ timeDelta);
};

var instance = new TestShip();