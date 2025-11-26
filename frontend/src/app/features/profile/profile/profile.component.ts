import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { UserService } from '../../../core/services/user.service';
import { User } from '../../../core/models/user.model';
import { Address, AddressCreate, AddressType } from '../../../core/models/address.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  user: User | null = null;
  addresses: Address[] = [];
  profileForm: FormGroup;
  addressForm: FormGroup;
  loading = false;
  error = '';
  success = '';
  showAddressForm = false;
  addressTypes = Object.values(AddressType);

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private userService: UserService
  ) {
    this.profileForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', Validators.required]
    });

    this.addressForm = this.fb.group({
      addressLine1: ['', Validators.required],
      addressLine2: [''],
      city: ['', Validators.required],
      state: ['', Validators.required],
      postalCode: ['', Validators.required],
      country: ['', Validators.required],
      addressType: [AddressType.HOME, Validators.required],
      isDefault: [false]
    });
  }

  ngOnInit(): void {
    this.user = this.authService.getCurrentUser();
    if (this.user) {
      this.profileForm.patchValue(this.user);
      this.loadAddresses(this.user.id);
    }
  }

  loadAddresses(userId: number): void {
    this.userService.getAddressesByUser(userId).subscribe({
      next: (addresses) => {
        this.addresses = addresses;
      },
      error: (error) => {
        console.error('Failed to load addresses:', error);
      }
    });
  }

  updateProfile(): void {
    if (this.profileForm.invalid || !this.user) {
      return;
    }

    this.loading = true;
    this.error = '';
    this.success = '';

    this.userService.updateUser(this.user.id, this.profileForm.value).subscribe({
      next: (updatedUser) => {
        this.user = updatedUser;
        this.success = 'Profile updated successfully';
        this.loading = false;
      },
      error: (error) => {
        this.error = error.message || 'Failed to update profile';
        this.loading = false;
      }
    });
  }

  addAddress(): void {
    if (this.addressForm.invalid || !this.user) {
      return;
    }

    const addressData: AddressCreate = {
      ...this.addressForm.value,
      userId: this.user.id
    };

    this.userService.createAddress(addressData).subscribe({
      next: (address) => {
        this.addresses.push(address);
        this.showAddressForm = false;
        this.addressForm.reset({ addressType: AddressType.HOME, isDefault: false });
        this.success = 'Address added successfully';
      },
      error: (error) => {
        this.error = error.message || 'Failed to add address';
      }
    });
  }

  deleteAddress(addressId: number): void {
    if (!confirm('Are you sure you want to delete this address?')) {
      return;
    }

    this.userService.deleteAddress(addressId).subscribe({
      next: () => {
        this.addresses = this.addresses.filter(a => a.id !== addressId);
        this.success = 'Address deleted successfully';
      },
      error: (error) => {
        this.error = error.message || 'Failed to delete address';
      }
    });
  }

  setDefaultAddress(addressId: number): void {
    if (!this.user) return;

    this.userService.setDefaultAddress(addressId, this.user.id).subscribe({
      next: () => {
        this.addresses.forEach(a => a.isDefault = a.id === addressId);
        this.success = 'Default address updated';
      },
      error: (error) => {
        this.error = error.message || 'Failed to update default address';
      }
    });
  }
}
