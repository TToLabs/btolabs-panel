// Arma www/ (lo que empaqueta Capacitor) copiando docs/ sin los .apk publicados en Pages.
// Sin este filtro, cada APK nueva lleva adentro la anterior y crece en cada compilación.
const fs = require('fs');
fs.rmSync('www', { recursive: true, force: true });
fs.cpSync('docs', 'www', { recursive: true, filter: (s) => !s.endsWith('.apk') });
console.log('www listo');
