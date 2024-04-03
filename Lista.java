const puppeteer = require('puppeteer');

async function extrairDados() {
  const browser = await puppeteer.launch();
  const page = await browser.newPage();
  await page.goto('https://pt.wikipedia.org/wiki/Lista_de_munic%C3%ADpios_do_Brasil_por_popula%C3%A7%C3%A3o');

  const dadosCidades = await page.evaluate(() => {
    const rows = Array.from(document.querySelectorAll('table.wikitable tbody tr'));
    rows.shift(); // Remover o cabeçalho da tabela

    return rows.map(row => {
      const columns = row.querySelectorAll('td');
      return {
        nome: columns[1].textContent.trim(),
        populacao: parseInt(columns[2].textContent.trim().replace(/\./g, ''), 10),
        uf: columns[0].textContent.trim()
      };
    });
  });

  await browser.close();

  return dadosCidades;
}

extrairDados().then(dados => {
  dados.forEach(cidade => {
    console.log('Nome:', cidade.nome);
    console.log('População:', cidade.populacao);
    console.log('UF:', cidade.uf);
    console.log('-----------------------');
  });
}).catch(error => {
  console.error('Erro ao extrair dados:', error);
});