const fs = require('fs')
const puppeteer = require('puppeteer')
const lighthouse = await import('lighthouse')

const waitTillHTMLRendered = async (page, timeout = 30000) => {
  const checkDurationMsecs = 1000;
  const maxChecks = timeout / checkDurationMsecs;
  let lastHTMLSize = 0;
  let checkCounts = 1;
  let countStableSizeIterations = 0;
  const minStableSizeIterations = 3;

  while(checkCounts++ <= maxChecks){
    let html = await page.content();
    let currentHTMLSize = html.length; 

    let bodyHTMLSize = await page.evaluate(() => document.body.innerHTML.length);

    //console.log('last: ', lastHTMLSize, ' <> curr: ', currentHTMLSize, " body html size: ", bodyHTMLSize);

    if(lastHTMLSize != 0 && currentHTMLSize == lastHTMLSize) 
      countStableSizeIterations++;
    else 
      countStableSizeIterations = 0; //reset the counter

    if(countStableSizeIterations >= minStableSizeIterations) {
      console.log("Fully Rendered Page: " + page.url());
      break;
    }

    lastHTMLSize = currentHTMLSize;
    await page.waitForTimeout(checkDurationMsecs);
  }  
};

async function captureReport() {
	//const browser = await puppeteer.launch({args: ['--allow-no-sandbox-job', '--allow-sandbox-debugging', '--no-sandbox', '--disable-gpu', '--disable-gpu-sandbox', '--display', '--ignore-certificate-errors', '--disable-storage-reset=true']});
	const browser = await puppeteer.launch({"headless": false, args: ['--allow-no-sandbox-job', '--allow-sandbox-debugging', '--no-sandbox', '--ignore-certificate-errors', '--disable-storage-reset=true']});
	const page = await browser.newPage();
	const baseURL = "http://localhost";
	
	await page.setViewport({"width":1920,"height":1080});
	await page.setDefaultTimeout(10000);
	
	const navigationPromise = page.waitForNavigation({timeout: 30000, waitUntil: ['domcontentloaded']});
	await page.goto(baseURL);
    await navigationPromise;
		
	const flow = await lighthouse.startFlow(page, {
		name: 'localhost',
		configContext: {
		  settingsOverrides: {
			throttling: {
			  rttMs: 40,
			  throughputKbps: 10240,
			  cpuSlowdownMultiplier: 1,
			  requestLatencyMs: 0,
			  downloadThroughputKbps: 0,
			  uploadThroughputKbps: 0
			},
			throttlingMethod: "simulate",
			screenEmulation: {
			  mobile: false,
			  width: 1920,
			  height: 1080,
			  deviceScaleFactor: 1,
			  disabled: false,
			},
			formFactor: "desktop",
			onlyCategories: ['performance'],
		  },
		},
	});
  
  // Step 1: Open the application
	await flow.navigate(baseURL, {
		stepName: 'open main page'
		});
		console.log('main page is opened');
	
	//Selectors
	const tableTab = 'a[href="/category/tables"]';
	const tableCart = 'a[href="/product/olive-table"]';
	const addButton = ".pro-details-quality > .pro-details-cart";
	const cartIcon = ".icon-cart";
	const cartPage = 'a[href="/cart"]';
	const checkoutPage = '.grand-totall>a:nth-child(4)';
	console.log(addButton);
	
  // Step 2: Navigate to "Tables" tab
	await flow.startTimespan({ stepName: 'open tables tab' });
		await page.click(tableTab);
		await navigationPromise;
		await waitTillHTMLRendered(page);
	await flow.endTimespan();
		console.log('Tables page is opened');
  
  // Step 3: Open a table product cart (click on a table)
	await flow.startTimespan({ stepName: 'open table cart' });
		await page.click(tableCart);
		await navigationPromise;
		await waitTillHTMLRendered(page);
	await flow.endTimespan();
		console.log('table cart is opened');

  // Step 4: Add table to Cart (click "Add to Cart" button)
	await flow.startTimespan({ stepName: 'add table to cart' });
		await page.click(addButton);
		await waitTillHTMLRendered(page);
	await flow.endTimespan();
		console.log('Table added to cart')

  // Step 5: Open Cart
	await flow.startTimespan({ stepName: 'open cart' });
		await page.waitForSelector(cartIcon, { timeout: 0 });
		await page.hover(cartIcon);//hover cart icon
		await page.click(cartIcon);
		page.on('dialog', async (dialog) => {
			await dialog.dismiss(); // Dismiss the dialog
		});
		await page.waitForSelector('.shopping-cart-btn'); // Wait for the cart icon to be fully loaded or visible
		await	page.waitForTimeout(1000);
		await page.click(cartPage);
		await navigationPromise;
		await waitTillHTMLRendered(page);
		await flow.endTimespan();
			console.log('Cart page opened'); 

  // Step 6: Click "Proceed to checkout"
	await flow.startTimespan({ stepName: 'open checkout' });
		await page.evaluate(() => {
			const element = document.querySelector(".grand-totall.cart-total-box");
			element.scrollIntoView();
		});
		await	page.waitForTimeout(1000);
		await page.hover(checkoutPage);
		await page.click(checkoutPage);
		await navigationPromise;
		await waitTillHTMLRendered(page);
	await flow.endTimespan();
		console.log('Checkout page opened');

  // Wait for the page to load completely
  //await page.waitForNavigation({ waitUntil: 'networkidle0' });

  //================================REPORTING================================
	const reportPath = __dirname + '/user-flow.report.html';
	//const reportPathJson = __dirname + '/user-flow.report.json';

	const report = await flow.generateReport();
	//const reportJson = JSON.stringify(flow.getFlowResult()).replace(/</g, '\\u003c').replace(/\u2028/g, '\\u2028').replace(/\u2029/g, '\\u2029');
	
	fs.writeFileSync(reportPath, report);
	//fs.writeFileSync(reportPathJson, reportJson);
	
    await browser.close();
}
captureReport();
