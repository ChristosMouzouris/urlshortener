export const NotificationEnum = {
  success: 'success',
  fail: 'fail',
} as const;

export type NotificationEnum =
  (typeof NotificationEnum)[keyof typeof NotificationEnum];
