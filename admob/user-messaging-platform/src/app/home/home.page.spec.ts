import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AdmobConsentStatus } from '@capacitor-community/admob';
import { PrivacyOptionsRequirementStatus } from '@capacitor-community/admob/dist/esm/consent/privacy-options-requirement-status.enum';

import { HomePage } from './home.page';

describe('HomePage', () => {
    let component: HomePage;
    let fixture: ComponentFixture<HomePage>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [HomePage],
        }).compileComponents();

        fixture = TestBed.createComponent(HomePage);
        component = fixture.componentInstance;
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should handle consent form during init', async () => {
        await component.ngOnInit();
        expect(component.status).toBe(AdmobConsentStatus.OBTAINED);
        expect(component.canRequestAds).toBeTrue();
        expect(component.isConsentFormAvailable).toBeTrue();
        expect(component.privacyOptionsRequirementStatus).toBe(PrivacyOptionsRequirementStatus.REQUIRED);
    });
});
